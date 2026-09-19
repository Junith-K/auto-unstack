# Auto Unstack

Automatically expands grouped Samsung notifications using Android Accessibility Service.

Auto Unstack is a lightweight utility that monitors the notification shade and automatically expands grouped notification stacks on Samsung devices. Never miss notifications hidden behind a badge count again.

## Features

- 🔄 **Automatic expansion** — Grouped notifications expand instantly when the notification shade opens
- 🔒 **Optional lock-screen support** — Expand grouped notifications while the phone is locked with a separate setting
- 📜 **Scroll-aware** — Newly visible grouped notifications are handled as you scroll
- 🔋 **Lightweight** — Minimal battery impact with smart event filtering
- 🛡️ **Privacy-first** — No personal data collection or network access
- ⚙️ **Independent settings** — Enable unlocked-shade and lock-screen behavior separately
- 📱 **Kotlin + Compose** — Modern, maintainable Android code

## How It Works

Auto Unstack uses Android's AccessibilityService to:
1. Check whether the unlocked-shade or lock-screen setting applies to the current lock state
2. Monitor the notification shade when SystemUI is the active window
3. Find Samsung's exact grouped-notification count element (`android:id/group_child_count_number`)
4. Tap only that element's clickable notification container
5. On the first unlocked-shade scan, collect visible groups and expand them from bottom to top without a delay between groups
6. After a 750 ms lock-screen wake settling period, and during later rescans, expand one visible group at a time and wait for SystemUI to update
7. Scan newly visible notifications when the user scrolls
8. Reset its handled-candidate list when the shade closes, the lock state changes, or the screen turns off/on

The service does not use message text, generic expand actions, or screen-position guesses to decide what to tap. It only uses screen bounds to order already verified groups from bottom to top. It only acts on the Samsung grouped-notification identifier and never scrolls the shade by itself.

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
- Notification UI structure (to find Samsung's grouped-notification identifier)
- Notification shade state
- Keyguard / lock state (to select the enabled mode)

## Installation

### From APK

1. Download the latest `app-release.apk` from [Releases](https://github.com/Junith-K/auto-unstack/releases)
2. Enable **Unknown Sources** in Settings → Security (if needed)
3. Install the APK on your device
4. Open Auto Unstack and enable the service
5. Enable Accessibility Service in Settings → Accessibility → Auto Unstack

### From Source

See [Build Instructions](#build-instructions) below.

## Usage

1. **Install and open the app**
2. **Choose where it runs** — Enable "Enable Auto Unstack", "Enable Lock Screen Auto Unstack", or both
3. **Enable Accessibility Service** — Go to Settings → Accessibility → Auto Unstack → toggle on
4. **Open or scroll the notification shade** — Visible grouped notifications will expand automatically

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
- **Lock-screen behavior is device-dependent** — Samsung SystemUI must expose the same grouped-notification identifier while locked
- **Limited to notification shade** — Does not work when the shade is closed
- **No automatic scrolling** — Scroll manually to reveal groups that are below the current viewport
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
