# JQ-010: Development environment

Researched on 2026-11-20 from Meta's and Android's documentation, plus an inventory of Marco's PC. Based on the stack proposed in [ADR-001](../decisions/ADR-001-technology-stack.md).

## Question

What must be installed on the PC and set up on the headset to build the app, install it on the Quest 3, and let Claude read logs and take screenshots?

## Short answer

Install **Android Studio** (it brings the Android SDK, the `adb` tool and its own Java), optionally **Meta Quest Developer Hub** to see the headset's view on the PC, and the **Oculus ADB driver** only if Windows does not see the headset. On the headset side: a Meta developer account, **developer mode**, and a USB-C **data** cable. Nothing else: no Platform SDK, no Meta Horizon Link, no game engine.

## Findings

### The PC today

| Item | State |
|---|---|
| Windows 11 Pro, AMD Ryzen 7 3700X, 64 GB RAM | Good for Android builds |
| Free disk space | C: 105 GB, E: 340 GB (Android Studio, SDK and caches need about 15 to 20 GB) |
| Git, Python, winget, Chocolatey | Installed |
| Java | JDK 25 installed and set as `JAVA_HOME`. The project will pin its own build JDK, so nothing changes system-wide |
| Gradle 8.10.2 | Installed globally, not needed: the project brings its own Gradle (the wrapper) |
| Android Studio, Android SDK, adb | **Missing** |
| Meta Quest Developer Hub | **Missing** |

### What to install

| Tool | Needed | Why | Where |
|---|---|---|---|
| Android Studio (latest stable) | **Yes** | Installs the Android SDK and `adb`, and includes a Java runtime for builds. Marco can use it to browse the code; Claude builds from the command line | https://developer.android.com/studio or `winget install --id Google.AndroidStudio -e` |
| Android SDK Platform 34 (Android 14) and Platform-Tools | **Yes** | Building for Horizon OS (Android 14) and talking to the headset | Android Studio, SDK Manager |
| Meta Quest Developer Hub (MQDH) | Recommended | Device manager, mirrors the headset's view on the PC, screenshots and video recordings, app install by drag and drop | https://developers.meta.com/vr/downloads/package/oculus-developer-hub-win/ |
| Oculus ADB driver | Only if needed | Windows USB driver for the headset; needed if `adb devices` shows nothing with the headset connected | https://developers.meta.com/vr/downloads/package/oculus-adb-drivers/ (unzip, right-click `android_winusb.inf`, Install) |
| Meta VR command line tool (`metavr`) | Later, maybe | Meta's command line tool for installs, logs and screenshots, with an interface for AI agents. `adb` covers milestone 1 | https://developers.meta.com/vr/essentials/metavr-install/ |

Not needed: the Meta Horizon Platform SDK (store services, see [JQ-008](JQ-008-quest3-platform.md)), Meta Horizon Link (PC VR), Unity, Unreal, Godot, the Meta Spatial Editor, the Android NDK (only for custom shaders, possibly later).

### Headset setup

Done once by Marco, described step by step in [JQ-008](JQ-008-quest3-platform.md#developer-mode-step-by-step-done-by-marco): developer account and team on developers.meta.com, verification, developer mode in the Meta Horizon phone app, USB debugging allowed in the headset.

A USB-C cable that carries **data** is needed for the first connection. After that, `adb` can also work over Wi-Fi.

### How Claude works with the headset

| Task | Command |
|---|---|
| Check the headset is connected | `adb devices` |
| Install or update the app | `adb install -r app-debug.apk` (or the Gradle task `installDebug`) |
| Start the app | `adb shell am start -n <package>/<activity>` |
| Read the app's log | `adb logcat --pid=$(adb shell pidof -s <package>)` |
| Screenshot | `adb shell screencap -p /sdcard/shot.png` then `adb pull /sdcard/shot.png` (or MQDH) |
| Short video | `adb shell screenrecord --time-limit 30 /sdcard/rec.mp4` then `adb pull` |
| Switch to Wi-Fi | `adb tcpip 5555`, then `adb connect <headset IP>:5555` |

Limits: screenshots are flat, so 3D depth and comfort can only be judged by Marco wearing the headset. Some captures need the headset to be worn (or its proximity sensor covered).

### Environment variables

`ANDROID_HOME` pointing to the SDK (`%LOCALAPPDATA%\Android\Sdk`) and the SDK's `platform-tools` folder in `PATH`, so that `adb` works in any terminal. Meta warns against having several copies of `adb` (MQDH ships its own): the SDK's copy should come first in `PATH`.

## Recommendation

1. Marco installs Android Studio and runs its setup wizard with the standard options, then adds Android 14 (API 34) in the SDK Manager.
2. Marco installs MQDH.
3. Marco enables developer mode and connects the headset by USB.
4. Claude sets `ANDROID_HOME` and `PATH` (with Marco's go-ahead), checks `adb devices`, and the Oculus ADB driver is installed only if the headset does not show up.

## Open points

- ~~Whether Windows 11 needs the Oculus ADB driver on this PC~~: not needed. On 2026-11-20 Windows 11 recognized the headset for `adb` without it. Android Studio Rabbit brings Java 25, which builds the project as is.
- Whether to adopt Meta's `metavr` tool for screenshots and logs: decided after milestone 1.

## Sources

Checked 2026-11-20.

- Meta Quest Developer Hub download: https://developers.meta.com/vr/downloads/package/oculus-developer-hub-win/
- MQDH media capture: https://developers.meta.com/vr/documentation/native/android/ts-mqdh-media/
- Oculus ADB driver: https://developers.meta.com/vr/downloads/package/oculus-adb-drivers/
- adb on Quest: https://developers.meta.com/vr/documentation/native/android/ts-adb/
- Logcat on Quest: https://developers.meta.com/vr/documentation/native/android/ts-logcat/
- Meta VR command line tool: https://developers.meta.com/vr/essentials/metavr-overview/
- Android adb and Platform-Tools: https://developer.android.com/tools/adb, https://developer.android.com/tools/releases/platform-tools
