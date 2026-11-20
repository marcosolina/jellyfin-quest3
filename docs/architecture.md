# Architecture

How the code is organized. Kept current with the code and checked at every end-of-iteration review ([process.md](process.md#code-quality)). The technology choices are in [ADR-001](decisions/ADR-001-technology-stack.md), [ADR-002](decisions/ADR-002-code-quality-tooling.md) and [ADR-003](decisions/ADR-003-hybrid-app.md).

## Overview

One Android app, written in Kotlin with Meta Spatial SDK, built by Gradle into a single APK for the Quest 3. Today (release 0.1.0) it is a single Gradle module, `app`, with one immersive activity that shows one floating panel. Milestone 2 turns it into a hybrid app: a window activity for browsing and an immersive activity for the cinema ([ADR-003](decisions/ADR-003-hybrid-app.md)). The code is split by feature into packages; it will be split into Gradle modules when the Jellyfin client arrives (see [Next steps](#next-steps)).

## Layout

```
jellyfin-quest3/
├── build.gradle.kts            root build: plugin versions, formatting (Spotless with ktlint)
├── settings.gradle.kts         repositories and modules
├── gradle/libs.versions.toml   every library and plugin version, pinned
├── .editorconfig               code style read by ktlint
└── app/
    ├── build.gradle.kts        Android settings, lint, dependencies, runOnQuest task
    ├── lint.xml                lint exceptions, each with its reason
    └── src/
        ├── main/
        │   ├── AndroidManifest.xml          Quest entries: immersive launch, head tracking, passthrough
        │   ├── kotlin/io/github/marcosolina/jellyfinquest/
        │   │   ├── JellyfinQuestActivity.kt  entry point: features, panels, scene setup
        │   │   ├── scene/                    placing things in the room
        │   │   ├── home/                     the home panel's content
        │   │   └── diagnostics/              decoder inventory (JQ-022)
        │   └── res/                          strings, panel id and theme, launcher icon
        └── test/kotlin/...                   unit tests, run on the PC
```

## Components

| Component | Package | Role |
|---|---|---|
| `JellyfinQuestActivity` | root | The only activity. Registers the Spatial SDK features (VR, Compose panels), creates the panel with its grab handle (Interaction SDK), owns the screen state (passthrough on or off, decoder report) and passes it down to the UI. When the headset is put on, it asks for the panel to be placed again in front of the viewer. |
| `PlaceAheadOfViewer` | `scene` | A Spatial SDK system that places an entity in front of the viewer once head tracking delivers a pose, and again on request; in between the entity stays fixed in the room, or where the viewer moved it with the handle. |
| `HomePanel` | `home` | Compose UI of the panel: greeting, version, passthrough button, decoder list. Receives plain values and callbacks. |
| `DecoderProbe` | `diagnostics` | Reads the headset's decoders from Android (`MediaCodecList`). |
| `HardwareReport` | `diagnostics` | Pure logic: turns the decoder list into one line per codec. Unit tested. |
| `HardwareSection` | `diagnostics` | Compose UI of the decoder list. |

## Rules

- **Spatial SDK stays at the edge.** Only the activity and the `scene` package import `com.meta.spatial`. UI and logic do not, so an SDK upgrade or a move to native OpenXR touches few files (ADR-001).
- **UI receives data, not services.** Composables get plain values and callbacks; state lives in the activity (later in view models).
- **Logic apart from Android.** Anything that can run without a device (rules, parsing, mapping) is plain Kotlin with unit tests; Android and SDK calls are thin wrappers around it.
- **Packages by feature**, not by technical layer.

## Build and checks

`gradlew build` runs everything below; a build with a violation fails.

| Check | Tool | Configuration |
|---|---|---|
| Formatting and style | Spotless with ktlint (`ktlint_official` style) | `build.gradle.kts`, `.editorconfig` |
| Static analysis | Android Lint, warnings as errors | `app/build.gradle.kts`, exceptions in `app/lint.xml` |
| Compiler warnings | Kotlin, warnings as errors | `app/build.gradle.kts` |
| Unit tests | JUnit 4 | `app/src/test` |

## Build types and signing

| Build type | App id | Name on the headset | Signed with |
|---|---|---|---|
| debug | `io.github.marcosolina.jellyfinquest.debug` | Jellyfin Quest (dev) | Android's debug key |
| release | `io.github.marcosolina.jellyfinquest` | Jellyfin Quest | The release key in `private/` |

The two are separate apps on the headset, so development builds never clash with the installed release. The release key and its passwords are in `private/release.keystore` and `private/signing.properties`, git-ignored; without them release builds come out unsigned. Losing the key means the next release cannot update the installed one (it must be uninstalled first).

## Commands

| What | Command |
|---|---|
| Full build with all checks (debug and release APKs) | `./gradlew build` |
| Fix formatting | `./gradlew spotlessApply` |
| Build, install the debug app on the headset and start it | `./gradlew runOnQuest` |
| Install a release on the headset | `adb install -r app/build/outputs/apk/release/app-release.apk` |
| App log | `adb logcat -s JellyfinQuest` |

The build runs on the Java that comes with Android Studio (or any JDK 21 or newer); the Android SDK location comes from `ANDROID_HOME` or `local.properties`.

## Next steps

When the Jellyfin client arrives (sign-in milestone), the pure Kotlin parts (Jellyfin access, playback rules) move to their own modules, so that they are tested without Android and the dependency direction is enforced by the build.
