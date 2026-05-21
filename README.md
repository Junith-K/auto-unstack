# Auto Unstack

Automatically expands grouped Samsung notifications using Android Accessibility Service.

Auto Unstack is a lightweight utility that monitors the notification shade and automatically expands grouped notification stacks on Samsung devices. Never miss notifications hidden behind a badge count again.

## Features

- 🔄 **Automatic expansion** — Grouped notifications expand instantly when the notification shade opens
- 🔋 **Lightweight** — Minimal battery impact with smart event filtering
- 🛡️ **Privacy-first** — No personal data collection or network access
- ⚙️ **Simple settings** — Single toggle to enable/disable
- 📱 **Kotlin + Compose** — Modern, maintainable Android code

## How It Works

Auto Unstack uses Android's AccessibilityService to:
1. Monitor the notification shade state
2. Detect numeric badges on grouped notifications (e.g., "2", "5", "12")
3. Identify clickable notification containers on the right side of the screen
4. Automatically tap to expand the grouped notification stack
5. Apply intelligent cooldown to prevent accidental double-clicks

The service runs only when the notification shade is active and stops processing once notifications are expanded.

## Accessibility Permission

This app requires the **Accessibility Service** permission to:
- Monitor notification shade state changes
- Access notification UI hierarchy
- Simulate user taps on notifications

**This permission does NOT allow:**
- Reading notification content or sensitive data
- Interfering with other app functionality
- Background activity outside the notification shade

See [Android Accessibility Service documentation](https://developer.android.com/reference/android/accessibilityservice/AccessibilityService) for more details.

## Privacy Statement

**Auto Unstack collects NO personal data:**
- No analytics tracking
- No crash reporting with personal info
- No network requests
- No telemetry
- All processing happens locally on your device

The app only accesses:
- Notification UI structure (to find badges)
- Notification shade state
- Screen dimensions

## Installation

### From APK

1. Download the latest `app-release.apk` from [Releases](https://github.com/Junith-J/auto-unstack/releases)
2. Enable **Unknown Sources** in Settings → Security (if needed)
3. Install the APK on your device
4. Open Auto Unstack and enable the service
5. Enable Accessibility Service in Settings → Accessibility → Auto Unstack

### From Source

See [Build Instructions](#build-instructions) below.

## Usage

1. **Install and open the app**
2. **Enable the toggle** — "Enable Auto Unstack" in the app settings
3. **Enable Accessibility Service** — Go to Settings → Accessibility → Auto Unstack → toggle on
4. **Pull down notification shade** — Grouped notifications will expand automatically

That's it! The service runs in the background and monitors your notification shade.

### Battery Optimization (Samsung)

On Samsung devices, battery optimization may block the Accessibility Service. If Auto Unstack stops working:

1. Go to Settings → Apps → Battery → Battery Optimization
2. Find "Auto Unstack"
3. Select "Don't optimize" or remove from optimization list
4. Restart the app

## Build Instructions

### Requirements

- Android Studio Koala or later
- Android SDK 29+ (for compilation)
- Kotlin 2.0+
- Gradle 9.4+

### Building Debug APK

```bash
./gradlew assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk`

### Building Release APK

First, create a keystore (if you haven't already):

```bash
keytool -genkey -v -keystore auto-unstack-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias auto-unstack
```

Then, set environment variables and build:

```bash
export KEYSTORE_PATH=auto-unstack-key.jks
export KEYSTORE_PASSWORD=your_password
export KEY_ALIAS=auto-unstack
export KEY_PASSWORD=your_key_password

./gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/app-release.apk`

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Accessibility**: Android AccessibilityService
- **Storage**: SharedPreferences
- **Minimum API**: 29
- **Target API**: 35

## Project Structure

```
auto-unstack/
├── app/
│   ├── src/main/
│   │   ├── java/com/autounstack/app/
│   │   │   ├── MainActivity.kt           # Settings screen (Compose)
│   │   │   ├── NotificationExpandService.kt  # Core service
│   │   │   └── PreferencesManager.kt     # SharedPreferences wrapper
│   │   ├── res/xml/
│   │   │   └── accessibility_service_config.xml  # Service configuration
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── gradle.properties
├── README.md
├── LICENSE
└── CONTRIBUTING.md
```

## Limitations

- **Works with Samsung notifications** — Designed specifically for Samsung's grouped notification stacks
- **Requires Accessibility Service** — Cannot function without this permission
- **Limited to notification shade** — Does not work when the shade is closed
- **No root required** — Uses standard Android Accessibility APIs

## Known Issues

- Battery optimization may interfere on some Samsung devices (see [Battery Optimization](#battery-optimization-samsung))
- Some third-party notification managers may not be compatible

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) file for details.

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

## Disclaimer

This app is provided "as-is" without warranty. Use at your own risk. The developer is not responsible for any device damage or data loss resulting from the use of this app.

## Contact

Have questions or found a bug? Open an issue on GitHub.

---

**Made with ❤️ for Samsung users who hate grouped notifications**
