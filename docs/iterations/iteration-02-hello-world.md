# Iteration 2: milestone 1, hello world on the Quest 3

**Status:** Closed · **Started:** 2026-11-20 · **Closed:** 2026-11-20 · **Released:** 0.1.0

## Goal

A minimal app, built with the chosen stack ([ADR-001](../decisions/ADR-001-technology-stack.md)), installed on Marco's Quest 3 from the PC with one command. It proves the whole chain (tools, build, developer mode, install, launch, logs) before any real feature is written.

## Prerequisites (Marco)

- Android Studio installed, with Android 14 (API 34) in the SDK Manager.
- Meta Quest Developer Hub installed (recommended).
- Developer mode enabled and the headset connected by a USB-C data cable.

Steps in [JQ-010](../research/JQ-010-development-environment.md) and [JQ-008](../research/JQ-008-quest3-platform.md).

## Scope

| ID | Type | Title |
|---|---|---|
| JQ-023 | Task | Set up the development environment and connect the headset |
| JQ-021 | Story | Hello world app running on the Quest 3 |
| JQ-013 | Story | Install the app from the PC with one command |
| JQ-022 | Spike | List the headset's video and audio decoders (small; moves to the next iteration if milestone 1 runs long) |
| JQ-025 | Story | Move the panel by its handle bar; place it in front of the viewer when the headset is put on (added during the test, at Marco's request) |

## Acceptance criteria

**JQ-023 Development environment**

- `adb devices` on the PC lists the Quest 3.
- `ANDROID_HOME` and `PATH` are set so that `adb` works in any terminal.

**JQ-021 Hello world**

- The project builds from a fresh clone with a single command, and the build includes formatting and static checks.
- On the Quest, the app appears in Library, Unknown Sources, with its name and icon.
- Starting it opens an immersive scene with a floating panel that shows a greeting and the app version, over passthrough (the real room).
- The panel has one button that switches passthrough off and on, to prove that controllers and hand pinch both work.
- Quitting and restarting the app works normally.
- `docs/architecture.md` describes the project structure; `CLAUDE.md` lists the build and install commands.

**JQ-013 One command install**

- One command on the PC builds the app, installs it on the connected headset and starts it.

**JQ-022 Decoders**

- The panel (and the log) lists the video decoders with their maximum resolution and whether they handle 10 bit and HDR, and the audio decoders (AC-3, E-AC-3 in particular). The results are added to [JQ-018](../research/JQ-018-video-on-quest3.md).

## Out of scope

Jellyfin, sign in, video playback, the cinema environment: they are the next milestones.

## Test checklist (Marco, on the headset)

Tested by Marco on 2026-11-20 with the debug build; the signed 0.1.0 release was then installed and started.

- [x] The app is in Library, Unknown Sources, with its name and icon, and starts from there.
- [x] The panel appears in front of the viewer over passthrough, with greeting and version.
- [x] "Hide the room" and "Show the room" work with the controller trigger and with a hand pinch.
- [x] The panel moves with its handle bar, with controllers and with hands.
- [x] Quitting and restarting the app works.
- [x] The decoder list is shown on the panel and written to the log.

Feedback from the test:

- The panel first appeared in a wrong place: the app had started while the headset was on the desk. Fixed by placing it again when the headset is put on (JQ-025).
- Marco could not move the panel: added the grab handle (JQ-025).
- The handle is a thin white bar; Marco asked for the bar of other Quest apps (title, close button). That bar is drawn by Horizon OS around 2D windows only, which led to [ADR-003](../decisions/ADR-003-hybrid-app.md) (hybrid app) and milestone 2.

## Phase log

| Phase | Status | Notes |
|---|---|---|
| Research | Done | Covered by iteration 1, plus the build setup of Meta's StarterSample and of the Hotaru project. |
| Plan | Done | Approved by Marco on 2026-11-20. |
| Code | Done | Branch `iteration/02-hello-world`. Versions follow a recent working Spatial SDK 0.15 project; manifest follows Meta's StarterSample; quality tooling in [ADR-002](../decisions/ADR-002-code-quality-tooling.md). JQ-025 added during the test at Marco's request. |
| Build | Done | `./gradlew build` passes formatting, lint (warnings as errors), Kotlin (warnings as errors) and 6 unit tests. |
| Test | Done | See the checklist above. |
| Release | Done | Version 0.1.0, signed with the release key in `private/` (git-ignored), tag `v0.1.0`, [CHANGELOG](../../CHANGELOG.md). Debug builds now use a separate app id (`.debug`, "Jellyfin Quest (dev)") so they never clash with the installed release. |
| Deploy | Done | The debug-signed copy was uninstalled once; the signed 0.1.0 is installed on the Quest 3 and starts. |
| Review | Done | See below. |

## Decoders found on the headset (JQ-022)

The results are recorded in [JQ-018](../research/JQ-018-video-on-quest3.md#confirmed-on-the-headset-2026-11-20).

## Code review

The code is small (8 source files, 565 lines including tests), formatted and lint clean.

| Finding | Action |
|---|---|
| `JellyfinQuestActivity` does four jobs: scene setup, the panel, decoder probing, passthrough state | Split in milestone 2, which separates a window activity from the immersive one (ADR-003). No separate task |
| The debug APK is 88 MB: Meta's native libraries are not stripped (no NDK installed) and code is not shrunk | New task JQ-027, low priority |
| `DecoderProbe` has no tests | By design: it is a thin wrapper over Android; the logic in `HardwareReport` is tested |
| The log line and the panel format decoder features separately | Accepted: two audiences (log and screen); the shared part (`maxSize`) is in one place |
| `runOnQuest` looks up `adb` when Gradle configures the build, so every build needs the Android SDK | Accepted: the SDK is needed to build anyway |
| `docs/architecture.md` did not describe the grab handle, the re-placement and the build types | Updated |

## Retrospective

- Went well: from no tools to a signed app on the headset in one session. Testing on the headset early found two real problems (placement, moving) that no build check could catch.
- Went less well: a typo in `local.properties` and a Spotless setting cost two failed builds; the first panel placement ignored the case of a headset lying on the desk.
- To keep: deploy to the headset after every small change while Marco is wearing it; it gives feedback in minutes.
