# Architecture

How the code is organized. Kept current with the code and checked at every end-of-iteration review ([process.md](process.md#code-quality)). The technology choices are in [ADR-001](decisions/ADR-001-technology-stack.md), [ADR-002](decisions/ADR-002-code-quality-tooling.md) and [ADR-003](decisions/ADR-003-hybrid-app.md).

## Overview

One Android app, written in Kotlin with Meta Spatial SDK, built by Gradle into a single APK for the Quest 3. Today (release 0.2.0) it is a single Gradle module, `app`, and a hybrid app ([ADR-003](decisions/ADR-003-hybrid-app.md)): it opens as a normal Horizon OS window for browsing, and switches to an immersive activity for the cinema and back. The code is split by feature into packages; it will be split into Gradle modules when the Jellyfin client arrives (see [Next steps](#next-steps)).

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
        │   ├── AndroidManifest.xml          the window (launcher) and the cinema (immersive), Quest entries
        │   ├── kotlin/io/github/marcosolina/jellyfinquest/
        │   │   ├── window/                   the launcher window: activity and screen
        │   │   ├── cinema/                   the immersive activity and its panel
        │   │   ├── scene/                    placing things in the room
        │   │   ├── diagnostics/              decoder inventory (JQ-022)
        │   │   └── ui/                       shared theme
        │   └── res/                          strings, panel id and theme, launcher icon, backup rules
        └── test/kotlin/...                   unit tests, run on the PC
```

## Components

| Component | Package | Role |
|---|---|---|
| `WindowActivity` | `window` | The launcher: a plain Compose activity shown by Horizon OS as a window with the system bar. Probes the decoders, starts the cinema, and knows how to be reopened when the cinema hands back. |
| `WindowScreen` | `window` | Compose UI of the window: greeting, version, Enter cinema, decoder list. |
| `CinemaActivity` | `cinema` | The immersive Spatial SDK activity. Registers the VR and Compose features, creates the control panel with its grab handle (Interaction SDK), owns the passthrough state, places the panel again when the headset is put on, and returns to the window. |
| `CinemaPanel` | `cinema` | Compose UI of the cinema panel: hide or show the room, back to the window. |
| `PlaceAheadOfViewer` | `scene` | A Spatial SDK system that places an entity in front of the viewer once head tracking delivers a pose, and again on request; in between the entity stays fixed in the room, or where the viewer moved it with the handle. |
| `DecoderProbe` | `diagnostics` | Reads the headset's decoders from Android (`MediaCodecList`). |
| `HardwareReport` | `diagnostics` | Pure logic: turns the decoder list into one line per codec. Unit tested. |
| `HardwareSection` | `diagnostics` | Compose UI of the decoder list. |
| `AppTheme` | `ui` | The shared Compose theme of window and panels. |

## Rules

- **Spatial SDK stays at the edge.** Only the `cinema` activity and the `scene` package import `com.meta.spatial`; the window does not use it at all. UI and logic do not, so an SDK upgrade or a move to native OpenXR touches few files (ADR-001).
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
| Build, install the debug app on the headset and open its window | `./gradlew runOnQuest` |
| Install a release on the headset | `adb install -r app/build/outputs/apk/release/app-release.apk` |
| App log | `adb logcat -s JellyfinQuest` |

The build runs on the Java that comes with Android Studio (or any JDK 21 or newer); the Android SDK location comes from `ANDROID_HOME` or `local.properties`.

## Next steps

When the Jellyfin client arrives (sign-in milestone), the pure Kotlin parts (Jellyfin access, playback rules) move to their own modules, so that they are tested without Android and the dependency direction is enforced by the build.

Offline viewing comes last (epic JQ-030), but two choices made earlier keep it cheap: the player is given a media source that can be either a stream from the server or a file on the headset, and watch progress goes through one component that can hold updates while offline and send them later.
