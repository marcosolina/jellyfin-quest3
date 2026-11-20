# Iteration 2: milestone 1, hello world on the Quest 3

**Status:** Active · **Started:** 2026-11-20

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

To be completed in the Test phase.

## Phase log

| Phase | Status | Notes |
|---|---|---|
| Research | Done | Covered by iteration 1. |
| Plan | Done | Approved by Marco on 2026-11-20. |
| Code | Waiting | Starts once the tools are installed and the headset is connected (JQ-023). |
| Build | | |
| Test | | |
| Release | | Version 0.1.0. |
| Deploy | | |
| Review | | Includes the first code structure review. |
