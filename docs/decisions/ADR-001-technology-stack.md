# ADR-001: Technology stack

**Status:** Accepted (2026-11-20)

## Context

The app is a virtual cinema for the Meta Quest 3: it must show flat films on a big screen, 3D films (side by side, over under) and 180 and 360 video, choosing the right mode for each file, with menus to sign in to Jellyfin and browse the library. Marco's library ([JQ-011](../research/JQ-011-server-and-media.md)) is mostly H.264 and HEVC in MKV and MP4, with AC-3 audio and many picture subtitles.

The sharpest way to show video in VR is to send the decoded frames straight to the headset's compositor rather than drawing them inside a 3D scene ([JQ-009](../research/JQ-009-technology-options.md)). The developer is an AI agent: text based code and command line builds matter, GUI-only editors do not fit. Marco has no prior experience with any of the options, so none has a head start.

## Options

1. **Meta Spatial SDK (Kotlin):** Meta's framework for immersive Android apps, with built-in video layers (flat, curved, 180, 360, stereo) and Compose panels.
2. **Unity with the Meta XR SDK (C#):** the most popular VR engine; sharp 3D and 360 video needs a paid plugin; scenes are edited in a GUI.
3. **Godot 4:** free and text based, but cannot play H.264 or HEVC without a custom Android plugin, and lacks stereo layers.
4. **Unreal Engine:** heavy, binary assets, poor fit.
5. **Native OpenXR (Kotlin and C++):** full control, but rendering, input and menus all written by hand.
6. **WebXR in the browser:** no MKV, no DTS, limited.

Pros and cons are compared in [JQ-009](../research/JQ-009-technology-options.md).

## Decision

**Meta Spatial SDK with Kotlin**, with this stack:

| Layer | Choice |
|---|---|
| Language | Kotlin |
| VR framework | Meta Spatial SDK (pinned version, currently 0.15.0), scene built in code, no Spatial Editor |
| Menus | Jetpack Compose panels |
| Video player | AndroidX Media3 (ExoPlayer), with Jellyfin's FFmpeg audio decoder and libass for ASS subtitles |
| Jellyfin client | Jellyfin Kotlin SDK (`org.jellyfin.sdk:jellyfin-core`) |
| Build | Gradle wrapper from the command line; Android Studio for Marco to browse and debug |
| Target | Android 14 (API 34), `arm64-v8a`, Quest 3 |

## Consequences

Easier:

- Every playback mode (flat, curved, 3D, 180, 360) uses Meta's own video layers, with hardware decoding and no extra copies.
- The Jellyfin SDK and the player of the official Jellyfin Android apps are reused as they are; their code is a reference for device profiles, playback and subtitles.
- Builds, installs, logs and screenshots all work from the command line.

Harder:

- Spatial SDK is young (0.x), changes every few weeks, and has a small community. Versions are pinned and SDK calls are kept behind a thin layer of our own, so an upgrade or a move to native OpenXR touches few files.
- Meta's SDK is closed source and its Gradle plugin sends usage telemetry.
- Subtitles need their own panel, because the video bypasses the normal Android views.
- HDR depends on server tone mapping at first.
- 3D correctness and comfort can only be checked by Marco on the headset: screenshots show no depth.

Ruled out for now: Unity, Godot, Unreal and WebXR. Native OpenXR stays as the fallback if Spatial SDK blocks a requirement.

## References

- [JQ-009 Technology options](../research/JQ-009-technology-options.md)
- [JQ-018 Video on the Quest 3](../research/JQ-018-video-on-quest3.md)
- [JQ-011 Marco's server and media](../research/JQ-011-server-and-media.md)
