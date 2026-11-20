# JQ-009: Technology options

Researched on 2026-11-20 from Meta, Unity, Godot, Epic, Android and Jellyfin documentation and GitHub repositories (sources at the end). Claims marked *(unconfirmed)* could not be verified in a current primary source. The decision is recorded in [ADR-001](../decisions/ADR-001-technology-stack.md).

## Question

Which technology should a Quest 3 virtual cinema with 3D and 180/360 video be built with, given that the media comes from Jellyfin (MKV and MP4, H.264 and HEVC, AC-3 audio, picture subtitles) and that the developer is an AI agent working with text files and command line builds?

## Short answer

**Meta Spatial SDK, in Kotlin.** It is the only option where Meta's own, documented API covers every requirement: hardware video decoding straight into the headset's compositor (the sharpest possible picture), side by side and over under 3D, 180 and 360 video in mono and stereo, passthrough, and real 2D menus with Jetpack Compose. It is the same Android and Kotlin world as the official Jellyfin Android apps, so the Jellyfin SDK and the video player they use drop in unchanged. Everything is text and builds from the command line.

## Findings

### What matters most for a cinema app: how the video reaches the eyes

There are two ways to show a video in VR:

1. **As a texture in the 3D scene:** the app decodes the video, copies each frame into the 3D engine and draws it on a surface in the scene. Easy and flexible, but the picture is resampled twice (by the engine, then by the headset's lens correction) and loses sharpness.
2. **As a compositor layer:** the decoded frames go straight to the headset's compositor, the part of the system that produces the final image for each eye, which places them on a flat, curved or spherical surface. No copy, no double resampling: the sharpest picture and the lowest power use. Meta's media apps work this way.

A virtual cinema should use compositor layers. The options differ mostly in how easily they give access to them.

### Comparison

| | Meta Spatial SDK | Unity + Meta XR SDK | Godot 4 | Unreal | Native OpenXR | WebXR (browser) |
|---|---|---|---|---|---|---|
| Language | Kotlin | C# | GDScript, C++ | C++, Blueprints | Kotlin + C++ | JavaScript |
| Cost | Free (closed SDK binaries, MIT samples) | Free under $200K revenue; good video plugin (AVPro) $250 to $900 | Free, MIT | Free (royalty above $1M) | Free | Free |
| Hardware decoding into a compositor layer | Yes, built in ("direct to surface") | Yes, with the paid plugin or low-level overlays | Only with a hand-written Android video plugin | Unclear on Quest | Yes, everything written by hand | Yes, through WebXR media layers |
| 3D side by side and over under | Built in (`StereoMode`) | With the paid plugin | Not directly on layers | Through materials (texture path) | By hand | Built in |
| 180 and 360, mono and stereo | Built in (`Equirect180`, `Equirect360`) | With the paid plugin or Meta samples | Mono only on layers | Through materials | By hand | In the specification; Quest support unconfirmed |
| MKV, AC-3, DTS, picture subtitles | Yes, through Media3 (the player of the Jellyfin Android apps) | Depends on the plugin | Through the custom plugin | Partly | Yes, through Media3 | No MKV, no DTS |
| Menus with posters | Jetpack Compose panels | Unity UI | Godot UI | Unreal UI | Build it yourself | Web pages |
| Command line build | Yes (`gradlew`) | Possible, needs an activated license | Yes | Heavy | Yes | No build needed |
| Fit for an AI agent working in text | High: all text, Meta ships agent tooling | Medium to low: scenes are edited in a GUI | Medium to high, but the video plugin is still Kotlin | Low: binary assets, huge toolchain | High, but the most code to write | High, but too limited |
| Maturity | Version 0.15 (pre 1.0), releases every 4 to 7 weeks | Most mature Quest engine | Weak on video | Mature engine, weak fit | Stable standard, few media samples | Mature, limited |

### Meta Spatial SDK in more detail

- **Version:** 0.15.0 (29 October 2026). Still below 1.0: releases every 4 to 7 weeks, sometimes with behaviour changes.
- **Video:** `VideoSurfacePanelRegistration` hands a drawing surface to the Media3 player; the SDK shows it as a compositor layer shaped as a flat quad, a curved cylinder, or a 180 or 360 sphere, with `StereoMode` `LeftRight` (side by side) or `UpDown` (over under).
- **Samples to learn from** ([Meta-Spatial-SDK-Samples](https://github.com/meta-quest/Meta-Spatial-SDK-Samples), MIT license):
  - **PremiumMediaSample:** Media3 player, direct-to-surface panels, 180 video, a cinema mode with dimmed passthrough; the whole scene is built in code.
  - **MediaPlayerSample:** flat and 360 stereo video.
  - **SpatialVideoSample:** side by side 3D on a custom panel.
  - **HybridSample:** starts as a 2D window and switches to immersive.
- **Meta Spatial Editor:** a visual scene editor. **Optional:** scenes can be built entirely in Kotlin, as PremiumMediaSample does. We will not use it.
- **Build:** Gradle plugin `com.meta.spatial.plugin`; the samples use JDK 17, Kotlin 2.1 and Android API 34. The plugin sends usage telemetry to Meta, with no documented opt-out.
- **Agent tooling:** the samples ship instructions for AI agents, and Meta's `metavr` command line tool installs apps, reads logs and takes screenshots.

### Pieces that come from the Jellyfin Android apps

The official Jellyfin apps for Android and Android TV use:

| Piece | Purpose | License |
|---|---|---|
| `androidx.media3` (ExoPlayer) 1.11 | Video player, hardware decoding, MKV, subtitles, HLS | Apache 2.0 |
| `org.jellyfin.media3:media3-ffmpeg-decoder` | Software decoding of AC-3, E-AC-3, DTS, TrueHD audio | GPL 3.0 |
| `org.jellyfin.sdk:jellyfin-core` (Jellyfin Kotlin SDK, 1.9.4) | Jellyfin API client: sign in, libraries, playback info | LGPL 3.0 |
| `io.github.peerless2012:ass-media` (libass) | Styled ASS subtitles | MIT (libass: ISC) |

All of them fit the Spatial SDK stack unchanged. The Jellyfin apps' code for device profiles, playback and subtitles is a useful reference (it is GPL, so copying it makes our code GPL too: see JQ-014).

### Why not the others

- **Unity:** the strongest alternative thanks to its community, but scenes are edited in a GUI, which an AI agent cannot drive or verify well, and sharp video with 3D and 360 needs a paid plugin.
- **Godot:** free and text based, but it cannot play H.264 or HEVC: it would need a custom Android video plugin anyway, and it lacks stereo on compositor layers.
- **Unreal:** heavy toolchain and binary assets, poor fit.
- **Native OpenXR:** full control, but everything Spatial SDK provides (rendering, input, panels) would have to be written by hand. It remains the **fallback**: the Kotlin Jellyfin and player code would carry over unchanged.
- **WebXR:** cannot play MKV directly or decode DTS, and would rely on server transcoding for most of the library.

## Recommendation

Meta Spatial SDK with Kotlin, the scene built in code (no Spatial Editor), Media3 for playback, the Jellyfin Kotlin SDK for the server, Jetpack Compose for menus. See [ADR-001](../decisions/ADR-001-technology-stack.md).

## Open points

- Whether Spatial SDK lets the app switch the headset to 72 Hz for 24 fps films (avoids judder; the system supports it) *(unconfirmed)*.
- Minimum Horizon OS version for Spatial SDK 0.15: not stated in the docs.
- License consequences of the GPL audio decoder next to Meta's closed SDK, if the app is ever distributed (JQ-014).

## Sources

Checked 2026-11-20.

- Spatial SDK release notes: https://developers.meta.com/horizon/documentation/spatial-sdk/release-notes
- Spatial SDK media playback: https://developers.meta.com/horizon/documentation/spatial-sdk/spatial-sdk-media-playback
- Spatial SDK samples: https://github.com/meta-quest/Meta-Spatial-SDK-Samples and the sample pages under https://developers.meta.com/horizon/documentation/spatial-sdk/
- Spatial Editor: https://developers.meta.com/horizon/documentation/spatial-sdk/spatial-sdk-editor/
- Meta AI tooling: https://developers.meta.com/horizon/essentials/ai-tooling-overview/ and https://github.com/meta-quest/agentic-tools
- Stereo surface composition for 2D apps: https://developers.meta.com/horizon/documentation/android-apps/stereo-surface-composition
- OpenXR runtime extensions on Quest 3: https://github.khronos.org/OpenXR-Inventory/runtime_extension_support.html
- Unity: https://developers.meta.com/horizon/documentation/unity/unity-sf-stereo180video/, https://docs.unity3d.com/Packages/com.unity.xr.compositionlayers@2.3/manual/android-surface-usage.html, https://unity.com/products/pricing-updates
- AVPro Video: https://www.renderheads.com/content/docs/AVProVideo-v3/articles/download.html
- Godot: https://docs.godotengine.org/en/stable/tutorials/animation/playing_videos.html, https://docs.godotengine.org/en/4.7/classes/class_openxrcompositionlayer.html
- Unreal Electra player: https://dev.epicgames.com/documentation/unreal-engine/electra-media-player-in-unreal-engine
- WebXR layers on Quest: https://developers.meta.com/horizon/documentation/web/webxr-layers/ and https://developers.meta.com/horizon/documentation/web/browser-video/
- Jellyfin Android apps' dependencies: `gradle/libs.versions.toml` in https://github.com/jellyfin/jellyfin-androidtv and https://github.com/jellyfin/jellyfin-android
- Jellyfin Kotlin SDK: https://github.com/jellyfin/jellyfin-sdk-kotlin
- libass for Media3: https://github.com/peerless2012/libass-android
