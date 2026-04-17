package com.example.runtrail.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.runtrail.domain.model.Run
import com.example.runtrail.ui.theme.RunTrailBlue
import com.example.runtrail.ui.theme.RunTrailDarkGray
import com.example.runtrail.ui.theme.RunTrailLightGray
import com.example.runtrail.ui.theme.RunTrailWhite
import com.example.runtrail.util.RunFormatter.formatPace
import com.example.runtrail.util.RunFormatter.formatRunDate

@Composable
fun RunSummaryCard(run: Run, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = RunTrailDarkGray),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = formatRunDate(run.timestamp),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = RunTrailWhite, fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${"%.2f".format(run.totalDistanceMeters / 1000f)} km",
                    style = MaterialTheme.typography.bodyMedium.copy(color = RunTrailLightGray)
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = formatPace(run.averagePaceMinPerKm),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = RunTrailBlue, fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "avg pace",
                    style = MaterialTheme.typography.labelSmall.copy(color = RunTrailLightGray)
                )
            }
        }
    }
}