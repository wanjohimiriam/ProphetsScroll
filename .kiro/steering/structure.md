# Project Structure

## Root Configuration

- `build.gradle.kts` - Root build configuration
- `settings.gradle.kts` - Project settings, module includes
- `gradle.properties` - Gradle properties
- `local.properties` - Local SDK paths (not in version control)

## App Module (`app/`)

### Build Configuration
- `build.gradle.kts` - App-level build config with dependencies
- `proguard-rules.pro` - ProGuard rules for release builds

### Source Code (`app/src/main/java/com/example/prophets_scroll/`)

#### Package Organization
```
com.example.prophets_scroll/
├── MainActivity.java              # Main app entry point
├── adapter/                       # RecyclerView adapters
│   └── CategoryAdapter.java
├── auth/                          # Authentication screens
│   ├── SignInActivity.java
│   └── SignUpActivity.java
└── onboard/                       # Onboarding flow
    ├── OnboardingActivity.java
    ├── PersonalizationStep1Activity.java
    ├── PersonalizationStep2Activity.java
    └── SetupCompleteActivity.java
```

### Resources (`app/src/main/res/`)

#### Layouts (`layout/`)
- Activity layouts for each screen
- Follow naming: `activity_<name>.xml`

#### Drawables (`drawable/`)
- `bg_*.xml` - Background drawables
- `btn_*.xml` - Button styles
- `ic_*.xml` - Vector icons
- `*.png` - Raster images (logo)

#### Values (`values/`)
- `colors.xml` - Color palette (gold/bronze theme)
- `strings.xml` - All text strings (English)
- `themes.xml` - App themes (Light default, Dark for auth)
- `dimens.xml` - Dimension values

#### XML Resources (`xml/`)
- `bg_*.xml` - Shape drawables
- `backup_rules.xml` - Backup configuration
- `data_extraction_rules.xml` - Data extraction rules

### Testing

- `app/src/test/` - Unit tests (JUnit)
- `app/src/androidTest/` - Instrumented tests (Espresso)

## Conventions

### Naming
- Activities: `<Feature>Activity.java`
- Layouts: `activity_<feature>.xml`
- Drawables: `<type>_<name>.xml` (e.g., `bg_button_primary.xml`, `ic_google.xml`)
- IDs: camelCase with prefix (e.g., `btnSignIn`, `tvTitle`, `etEmail`)

### Code Organization
- Group related features in packages (`auth/`, `onboard/`)
- Adapters in dedicated `adapter/` package
- Keep activities focused on UI logic
- Use Material Design 3 components

### Resource Management
- All user-facing strings in `strings.xml` (no hardcoded text)
- Colors defined in `colors.xml` with semantic names
- Use content descriptions for accessibility (`cd_*` strings)
