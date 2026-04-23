# 🏃 RunTrail

> The application prioritizes signal integrity, uninterrupted tracking, and minimal UI noise.
> Complex telemetry is reduced into a clean, high-contrast interface designed for outdoor visibility and immediate readability.

[![Platform](https://img.shields.io/badge/platform-Android-blue)](https://developer.android.com)
[![Language](https://img.shields.io/badge/language-Kotlin-orange)](https://kotlinlang.org)
[![UI](https://img.shields.io/badge/UI-Jetpack%20Compose-green)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/architecture-Clean%20Architecture-lightgrey)](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html)
[![License](https://img.shields.io/badge/license-MIT-black)](LICENSE)

- RunTrail is a precision-focused GPS running tracker engineered for reliability under real-world constraints. 
- It records routes, pace, distance, and elevation while maintaining consistent background execution even under aggressive system limitations.


---

## Features

### 1. Tracking

* Continuous GPS route recording with live polyline rendering
* Real-time pace, distance, and elapsed time calculation
* Elevation-aware tracking (device dependent)

### 2. Reliability

* Foreground service resistant to background termination
* Mid-run persistence to prevent data loss on crashes
* GPS signal filtering to reduce drift and spikes

### 3. History & Insights

* Persistent run history with detailed summaries
* Aggregated metrics across sessions
* Map-based replay of recorded routes

### 4. UI/UX

* Minimalist, distraction-free interface
* High-contrast design optimized for outdoor usage
* Large typography for real-time metrics

### 5. Advanced

* Location smoothing via filtering logic
* Audio coaching via Text-to-Speech (distance milestones)
* Custom Canvas charts for weekly distance visualization


---

## Tech Stack

### *Core*

* **Language:** Kotlin
* **UI:** Jetpack Compose
* **Architecture:** Clean Architecture + MVVM

### *Data & State*

* **Local Storage:** Room Database
* **Async:** Coroutines + Flow
* **State Management:** StateFlow

### *System & Services*

* **Location:** FusedLocationProviderClient
* **Maps:** Google Maps SDK
* **Background Execution:** Foreground Service + NotificationManager

### *Dependency Injection*

* **Hilt**


---

## Screenshots

| Home | History | StartRun |
|------------|-----------|--------------|
| <img width="250" alt="Home" src="https://github.com/user-attachments/assets/0fc842cb-41d0-4ff5-8745-b9ace6106399" /> | <img width="250" alt="history" src="https://github.com/user-attachments/assets/77de10c1-a48b-47c8-a0fa-c7ec4f7eb7a3" /> | <img width="250" alt="startRun" src="https://github.com/user-attachments/assets/1f482c86-08c4-4494-9757-828461a2bec0" /> |


---

## Architecture & Logic Overview

<img width="800" alt="runTrail Arch Logic" src="https://github.com/user-attachments/assets/c1617c6f-5574-44d1-9bed-c1ac246edd66" />

RunTrail follows strict separation of concerns using Clean Architecture mapped to MVVM.

```
FusedLocationProviderClient
        ↓
Repository (Flow)
        ↓
UseCases (Domain Layer)
        ↓
ViewModel (StateFlow)
        ↓
Jetpack Compose UI
```

### Layers

**Presentation**

* Compose UI
* ViewModels
* Reactive state via StateFlow

**Domain**

* Business logic (UseCases)
* Pure Kotlin, no framework dependencies

**Data**

* Repository implementations
* Room database
* Location data sources


---

## Performance Considerations

* Batched database inserts during active tracking
* Controlled recomposition in Compose
* Efficient polyline rendering for large datasets


---

## Security & Privacy

* Fully offline architecture
* No external APIs or data transmission
* Location permissions handled with explicit user consent


