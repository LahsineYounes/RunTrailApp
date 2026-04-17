package com.example.runtrail.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.runtrail.MainActivity
import com.example.runtrail.R
import com.example.runtrail.data.location.LocationDataSource
import com.example.runtrail.domain.model.LocationPoint
import com.example.runtrail.domain.model.Run
import com.example.runtrail.domain.model.RunState
import com.example.runtrail.domain.usecase.CalculatePaceUseCase
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

@AndroidEntryPoint
class RunTrackingService : Service() {

    @Inject lateinit var locationDataSource: LocationDataSource
    @Inject lateinit var calculatePaceUseCase: CalculatePaceUseCase

    // Use Main.immediate for state updates to ensure UI sees them instantly
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private var startTimeWallClock = 0L
    private var startTimeElapsed = 0L
    private var totalDistanceMeters = 0f
    private var lastLocation: LocationPoint? = null
    private var timerJob: Job? = null
    private var locationJob: Job? = null

    private lateinit var wakeLock: PowerManager.WakeLock
    private val notificationManager by lazy { getSystemService(NotificationManager::class.java) }

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): RunTrackingService = this@RunTrackingService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "RunTrail::TrackingWakeLock")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startTracking()
            ACTION_PAUSE -> pauseTracking()
            ACTION_RESUME -> resumeTracking()
            ACTION_STOP -> stopTracking()
        }
        return START_STICKY
    }

    private fun startTracking() {
        startTimeWallClock = System.currentTimeMillis()
        startTimeElapsed = SystemClock.elapsedRealtime()
        totalDistanceMeters = 0f
        lastLocation = null
        pendingLocationPoints.clear()

        _runState.value = RunState.Running(0L, 0f, 0f, emptyList())

        // Use ServiceCompat for Android 14+ support
        ServiceCompat.startForeground(
            this,
            NOTIFICATION_ID,
            buildNotification("0:00", "0.00 km"),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            } else 0
        )

        if (!wakeLock.isHeld) wakeLock.acquire(10 * 60 * 60 * 1000L)

        launchTimerJob()
        launchLocationJob()
    }

    private fun pauseTracking() {
        locationJob?.cancel()
        timerJob?.cancel()
        val current = _runState.value
        if (current is RunState.Running) {
            _runState.value = RunState.Paused(
                durationMillis = SystemClock.elapsedRealtime() - startTimeElapsed,
                distanceMeters = totalDistanceMeters,
                locationPoints = current.locationPoints
            )
        }
        updateNotification("PAUSED", "${"%.2f".format(totalDistanceMeters / 1000f)} km")
    }

    private fun resumeTracking() {
        val paused = _runState.value as? RunState.Paused ?: return
        // Adjust elapsed start time to account for pause duration
        startTimeElapsed = SystemClock.elapsedRealtime() - paused.durationMillis
        launchTimerJob()
        launchLocationJob()
    }

    private fun stopTracking() {
        serviceScope.coroutineContext.cancelChildren()
        if (wakeLock.isHeld) wakeLock.release()

        val finalDuration = SystemClock.elapsedRealtime() - startTimeElapsed
        val avgPace = calculatePaceUseCase(totalDistanceMeters, finalDuration)

        val completedRun = Run(
            id = UUID.randomUUID().toString(),
            timestamp = startTimeWallClock,
            durationInMillis = finalDuration,
            totalDistanceMeters = totalDistanceMeters.toInt(),
            averagePaceMinPerKm = avgPace,
            locationPoints = pendingLocationPoints.toList()
        )

        _runState.value = RunState.Finished(completedRun)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun launchTimerJob() {
        timerJob?.cancel()
        timerJob = serviceScope.launch {
            while (isActive) {
                val elapsed = SystemClock.elapsedRealtime() - startTimeElapsed
                val current = _runState.value
                if (current is RunState.Running) {
                    val pace = calculatePaceUseCase(totalDistanceMeters, elapsed)
                    _runState.value = current.copy(
                        durationMillis = elapsed,
                        currentPaceMinPerKm = pace
                    )
                }
                updateNotification(formatTime(elapsed), "${"%.2f".format(totalDistanceMeters / 1000f)} km")
                delay(1000L)
            }
        }
    }

    private fun launchLocationJob() {
        locationJob?.cancel()
        locationJob = serviceScope.launch {
            locationDataSource.getLocationUpdates(1000L).collect { newPoint ->
                lastLocation?.let { prevPoint ->
                    val delta = FloatArray(1)
                    Location.distanceBetween(
                        prevPoint.latitude, prevPoint.longitude,
                        newPoint.latitude, newPoint.longitude,
                        delta
                    )

                    val timeDeltaSec = (newPoint.timestamp - prevPoint.timestamp) / 1000f
                    val speed = if (timeDeltaSec > 0) delta[0] / timeDeltaSec else 0f

                    if (speed < 15f) { // Filtering jumps over 54 km/h
                        totalDistanceMeters += delta[0]
                    }
                }
                lastLocation = newPoint
                pendingLocationPoints.add(newPoint)

                val current = _runState.value
                if (current is RunState.Running) {
                    _runState.value = current.copy(
                        distanceMeters = totalDistanceMeters,
                        locationPoints = pendingLocationPoints.toList() // Consider optimizing if list gets very large
                    )
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "Run Tracking",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(time: String, distance: String): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java).apply {
                putExtra("navigate_to", "active_run")
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("RunTrail — Recording")
            .setContentText("$time  •  $distance")
            .setSmallIcon(R.drawable.ic_run_notification)
            .setOngoing(true)
            .setSilent(true) // Ensures no sound on updates
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun updateNotification(time: String, distance: String) {
        notificationManager.notify(NOTIFICATION_ID, buildNotification(time, distance))
    }

    private fun formatTime(millis: Long): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / (1000 * 60)) % 60
        val hours = (millis / (1000 * 60 * 60))
        return if (hours > 0) {
            "%d:%02d:%02d".format(hours, minutes, seconds)
        } else {
            "%d:%02d".format(minutes, seconds)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        if (wakeLock.isHeld) wakeLock.release()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_RESUME = "ACTION_RESUME"
        const val ACTION_STOP = "ACTION_STOP"

        const val CHANNEL_ID = "run_tracking_channel"
        const val NOTIFICATION_ID = 1

        private val _runState = MutableStateFlow<RunState>(RunState.Idle)
        val runState: StateFlow<RunState> = _runState.asStateFlow()

        val pendingLocationPoints = mutableListOf<LocationPoint>()
    }
}