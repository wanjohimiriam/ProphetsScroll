# Technology Stack

## Build System

- **Build Tool**: Gradle with Kotlin DSL (`.gradle.kts`)
- **Android Gradle Plugin**: Managed via version catalog (`libs.versions.toml`)
- **Java Version**: Java 11 (source & target compatibility)

## SDK & Platform

- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36
- **Compile SDK**: 36
- **Package**: `com.example.prophets_scroll`

## Core Dependencies

- `androidx.appcompat` - AppCompat library
- `com.google.android.material` - Material Design 3 components
- `androidx.activity` - Activity APIs
- `androidx.constraintlayout` - ConstraintLayout

## Testing

- `junit` - Unit testing
- `androidx.test.ext:junit` - Android JUnit extensions
- `androidx.test.espresso:espresso-core` - UI testing

## Common Commands

### Build & Run
```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install on connected device
./gradlew installDebug

# Clean build
./gradlew clean
```

### Testing
```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Run all tests
./gradlew check
```

### Dependency Management
```bash
# View dependency tree
./gradlew app:dependencies

# Check for dependency updates
./gradlew dependencyUpdates
```

## Permissions

- `INTERNET` - Required for network operations (authentication, content sync)
