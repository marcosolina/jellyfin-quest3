# JQ-008: Quest 3 platform basics

Researched on 2026-11-20 from Meta's developer documentation (sources at the end). Claims marked *(unconfirmed)* come from secondary sources or could not be verified in an official page.

## Question

How does the Quest 3 run apps, what kind of app can we build, what must an app satisfy to be installed from a PC, and how does a personal app get onto the headset?

## Short answer

The Quest 3 runs **Horizon OS, which is Android 14 (API level 34)** with Meta's VR layers on top. Apps are ordinary Android packages (APK) for 64 bit ARM. A personal app is installed from the PC (**sideloading**) after enabling **developer mode**, which needs a free Meta developer account, a developer "team" and a one-time verification (SMS number or payment card). The **Platform SDK** linked from Meta's downloads page is for store services and is **not needed** for this project.

## Findings

### Naming

Meta renamed its developer site and tools during 2026: `developers.meta.com/horizon` became `developers.meta.com/vr`, with "Meta VR" branding. Meta Quest Developer Hub (MQDH) also appears as Meta VR Developer Hub (MVDH). Old and new names refer to the same products.

### Horizon OS

- Restarted its version numbering as Horizon OS 2.x in February 2026; 2.10 is rolling out in November 2026 *(exact current stable version unconfirmed)*.
- Based on **Android 14 (API 34)** since April 2025.
- No Google services: Android APIs that depend on Google Play Services do not work.
- Meta's own SDK level on a headset (207 in September 2026) can be read with `adb shell getprop ro.vros.build.version`.

### Kinds of apps

| Kind | What it is | How it is built |
|---|---|---|
| 2D panel app | A normal Android app shown as a floating window in the Quest home, used with hands (look and pinch) or controllers | Android Studio, Kotlin, Jetpack Compose |
| Immersive app | Takes over the whole view and draws its own 3D world, through the OpenXR standard | Meta Spatial SDK (Kotlin), Unity, Unreal, Godot, or native OpenXR (C++) |
| Spatial SDK app | Meta's Kotlin framework for immersive and mixed reality apps that can also host 2D panels | Android Studio, Kotlin |
| Web app | WebXR pages in the Quest browser, optionally packaged as an installable web app | Browser technologies |

A virtual cinema is an **immersive app**. Which framework to use is the subject of JQ-009.

### What an APK needs for the Quest

| Requirement | Value |
|---|---|
| Processor architecture | `arm64-v8a` only |
| `minSdk` / `targetSdk` | Recommended 32 / 34 (accepted: min 29 to 34; target 32 to 34 for immersive apps) |
| Immersive launch | Activity category `com.oculus.intent.category.VR` (panel apps must not declare it) |
| Head tracking | `android.hardware.vr.headtracking` feature, `required="true"` for immersive apps |
| Supported devices | `com.oculus.supportedDevices` metadata including `quest3`, otherwise the app runs in compatibility mode |
| OpenXR | The standard Khronos OpenXR loader for Android (1.0.34 or later); no proprietary loader |
| Signature | The APK must be signed; a debug signature is enough for sideloading |

Store-only rules (release builds only, unique labels, and so on) do not apply to sideloaded apps.

### Developer mode, step by step (done by Marco)

1. Sign up at developers.meta.com with the **same Meta account used on the headset**.
2. Create a developer **team** (organization) in the developer dashboard: required before verification.
3. **Verify** the account once: an SMS capable mobile number, or a payment card (PayPal is not accepted). Minimum age 18.
4. In the **Meta Horizon phone app**: headset icon, choose the Quest 3, Headset settings, **Developer mode**, on.
5. Connect the Quest to the PC with a USB-C **data** cable (charge-only cables do not work), put the headset on and accept **"Always allow from this computer"** when it asks to allow USB debugging.

Caveats:

- Meta now asks some developers to verify the organization with an ID document. Its own setup pages still list only SMS or card verification for developer mode, but a Meta reply from April 2026 said a Quest 3S needed a verified organization *(unconfirmed whether this applies to the Quest 3)*.
- Forum users reported "Unexpected error" or a missing toggle when enabling developer mode in 2026, without a published fix.
- With developer mode on, the headset scans installed apps for malware by default.
- Meta's terms: developer mode is meant for developing and testing apps; other use "may lead to account limitations". Installing our own app is exactly the intended use.

### Sideloaded apps on the headset

- They appear in the **Library**, under **Unknown Sources** (the tab shows up once at least one such app is installed). They can also be started from MQDH or from the command line.
- They are not covered by Meta's cloud backup and get no automatic updates: updates are installed from the PC.
- Some users report the Unknown Sources tab disappearing after system updates until a reboot *(community reports)*.

### Platform SDK

The page Marco linked is the **Meta Horizon Platform SDK** (still titled "Oculus Platform SDK", version 207, September 2026). It provides store and social services: entitlement checks, in-app purchases, achievements, leaderboards, matchmaking, voice chat, cloud backup. Every feature needs an app registered in Meta's developer dashboard, so it is meant for store apps. A personal, sideloaded Jellyfin client does not use any of these services: **not needed**.

## Recommendation

- Target an **immersive app** for Android 14, `arm64-v8a`, minSdk 32, targetSdk 34, with the Quest manifest entries above.
- Marco enables developer mode now (steps above), because milestone 1 needs it and the verification may take time if Meta asks for more.
- Skip the Platform SDK.

## Open points

- Whether the Quest 3 asks for organization ID verification: discovered when Marco enables developer mode.
- Current Horizon OS version on Marco's headset: read it once the headset is connected.

## Sources

Checked 2026-11-20.

- Platform SDK download: https://developers.meta.com/vr/downloads/package/oculus-platform-sdk/
- Platform SDK setup for Android: https://developers.meta.com/vr/documentation/android-apps/ps-setup/
- Manifest requirements: https://developers.meta.com/vr/resources/publish-mobile-manifest/ and https://developers.meta.com/vr/documentation/native/android/mobile-native-manifest/
- OpenXR on Android: https://developers.meta.com/vr/documentation/native/android/mobile-openxr
- Android 14 requirement: https://developers.meta.com/vr/blog/meta-quest-apps-android-14-march-1/
- Features of 2D apps: https://developers.meta.com/vr/documentation/android-apps/features-overview
- Device setup: https://developers.meta.com/vr/documentation/native/android/mobile-device-setup-quest/
- Enable developer mode: https://developers.meta.com/vr/documentation/android-apps/enable-developer-mode/
- Organization verification: https://developers.meta.com/vr/resources/publish-organization-verification/
- Malware scanning: https://developers.meta.com/vr/resources/malware-detection/
- adb troubleshooting (cloud backup note): https://developers.meta.com/vr/documentation/native/android/ts-adb/
- Secondary: UploadVR on Horizon OS 2.10 (https://www.uploadvr.com/quests-latest-ptc-brings-streamlined-interface-theater-environment/), Meta community forums on developer mode issues.
