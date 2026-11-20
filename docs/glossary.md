# Glossary

Technical terms used in this project, in plain language. New terms are added the first time they appear in the research, the plans or the code.

## Quest platform

| Term | Meaning |
|---|---|
| Horizon OS | The operating system of the Meta Quest headsets. It is Android 14 with extra VR capabilities, so Quest apps are Android apps. |
| APK | Android Package: the single file that contains an Android app. It is what gets installed on the Quest. |
| Sideloading | Installing an app from the PC instead of the Meta store. It is how this app will be installed. |
| Developer mode | A headset setting that allows sideloading and debugging. It is enabled from the Meta Horizon phone app, after registering a free Meta developer account and team. |
| ADB | Android Debug Bridge: a command line tool on the PC that talks to an Android device (the Quest) over USB or Wi-Fi, to install apps, read logs and take screenshots. |
| MQDH | Meta Quest Developer Hub (also called Meta VR Developer Hub): Meta's desktop app for working with a headset during development (install apps, see logs, mirror the view). |
| Platform SDK | Meta's library for store services (purchases, achievements, matchmaking). Not used by this project. |
| 2D panel app | An app that runs in a flat window inside the Quest home environment, like a phone or tablet app on a large floating screen. |
| Immersive app | An app that takes over the whole view and draws its own 3D world, for example a virtual cinema. |
| OpenXR | The industry standard programming interface between VR apps and headsets. Immersive Quest apps use it, directly or through a framework. |
| Compositor | The part of the headset's system that builds the final picture for each eye, combining what apps send it and correcting for the lenses. |
| Compositor layer | A picture (a video frame, a menu) handed directly to the compositor, which places it on a flat, curved or spherical surface. Sharper than drawing the same picture inside the 3D scene, because it is resampled only once. |
| Passthrough | Showing the real room through the headset's cameras, so virtual objects appear in the real world (mixed reality). |
| Panel | In Spatial SDK, a flat surface in the 3D scene that shows normal Android screens, such as menus. |
| Refresh rate | How many times per second the headset's displays update (72, 90 or 120 Hz on Quest 3). Films are 24 frames per second: at 72 Hz each frame is shown exactly 3 times; at 90 Hz the timing is uneven, which causes visible stutter called judder. |

## Building the app

| Term | Meaning |
|---|---|
| Kotlin | The main programming language for Android apps. This app is written in it. |
| Android SDK | Google's libraries and tools for building Android apps, installed with Android Studio. |
| Android Studio | Google's application for writing, building and debugging Android apps. |
| Gradle | The build tool of Android projects: it compiles the code and produces the APK. The Gradle wrapper (`gradlew`) downloads the right Gradle version automatically. |
| Meta Spatial SDK | Meta's Kotlin framework for immersive and mixed reality apps on Quest, with built-in video layers. The chosen framework ([ADR-001](decisions/ADR-001-technology-stack.md)). |
| Jetpack Compose | Android's toolkit for building screens and menus in Kotlin code. |
| Jellyfin Kotlin SDK | The official Kotlin library for talking to a Jellyfin server. |
| Media3 / ExoPlayer | Android's video player library, also used by the official Jellyfin Android apps. |
| MediaCodec | The Android interface to the device's hardware video and audio decoders. |
| FFmpeg | An open source library that decodes almost any audio and video format, in software. Used here for AC-3, DTS and TrueHD audio. |
| libass | An open source library that draws styled ASS subtitles. |

## Video

| Term | Meaning |
|---|---|
| Codec | The method used to compress video or audio. Common video codecs: H.264 (AVC), H.265 (HEVC), VP9, AV1. Common audio codecs: AAC, AC-3 (Dolby Digital), E-AC-3, DTS, Opus. |
| Container | The file format that packs video, audio and subtitle tracks together: MKV, MP4, AVI. The container is not the codec: an MKV file can hold H.264 or H.265 video. |
| Encoding and decoding | Encoding compresses the pictures into a codec; decoding turns the compressed data back into pictures to show. A player only needs to decode. |
| Hardware decoding | Decoding done by a dedicated chip instead of the main processor. Fast, cool and light on the battery, but only for the codecs the chip supports. |
| Software decoding | Decoding done by the main processor. Works for more formats, but uses more power and may not keep up with high resolutions. |
| Bitrate | How much data each second of video uses. Higher bitrate means better quality and more network bandwidth. |
| 10 bit | Video that stores each colour with more precision than ordinary 8 bit video; used by HDR and by many HEVC encodes. |
| HDR | High Dynamic Range (HDR10, HDR10+, Dolby Vision): video with a wider range of brightness and colour. It needs support in both the decoder and the display. |
| Dolby Vision profiles | Variants of Dolby Vision. Profile 8 contains an ordinary HDR10 picture that other players can use; profile 7 (from Blu-ray discs) uses two layers and needs special handling. |
| SDR | Standard Dynamic Range: ordinary, non-HDR video and displays. The Quest 3 display is SDR. |
| Tone mapping | Converting HDR video so that it looks right on a screen that does not support HDR. |
| Remux | Moving the tracks into a different container without re-encoding them. Fast and without quality loss. |
| Picture subtitles | Subtitles stored as images (DVD VobSub, Blu-ray PGS) rather than text. The player shows them as pictures; they cannot be restyled. |
| 3D: SBS and OU | Stereoscopic video that packs the left eye and right eye pictures into one frame, either side by side (SBS) or over under (OU, also called top and bottom, TB or TAB). |
| Half and full SBS/OU | In half SBS, each eye's picture is squeezed to half width so the frame keeps a normal size; in full SBS each eye keeps full width, so the frame is twice as wide (32:9 for a 16:9 film). |
| MVC and MV-HEVC | Codecs that store two views of a 3D video in one stream: MVC on Blu-ray 3D discs, MV-HEVC in Apple's spatial videos. |
| 180 and 360 video | Video that surrounds the viewer, half sphere (180) or full sphere (360), mono or stereo. |
| Equirectangular | The usual way to store 180 and 360 video in a flat frame, like a world map: the player wraps it back onto a sphere. |

## Jellyfin

| Term | Meaning |
|---|---|
| Direct play | The client plays the original file as it is. Best quality, no work for the server. |
| Direct stream | The server remuxes the file on the fly into a container the client accepts, without re-encoding the video. |
| Transcoding | The server re-encodes video or audio into a format the client can play. Costs server processing power and some quality; used only when needed. |
| Device profile | The description a client sends to the server of what it can play (containers, codecs, resolutions, bitrates). The server uses it to choose between direct play, direct stream and transcoding. |
| PlaybackInfo | The Jellyfin API call where the client sends its device profile and the server answers how a given item will be played. |
| Quick Connect | A Jellyfin way to sign in a new device by confirming a short code from a device that is already signed in, instead of typing the password. Useful in VR, where typing is awkward. |
| Video3DFormat | The Jellyfin field that tells whether a video is 3D and how (half or full, side by side or top and bottom, MVC). Jellyfin sets it mostly from tags in the file name, such as `.3D.hsbs.`. |
| NFO file | A small XML file next to a video with its metadata, read by Jellyfin. |

## Project

| Term | Meaning |
|---|---|
| Iteration | One round of the cycle described in [process.md](process.md), with a single goal. |
| Milestone | A small, testable step of the app, delivered by one iteration. |
| Spike | A research work item with a clear question; its answer is a file in [research/](research/). |
| ADR | Architecture Decision Record: a short document that records a significant technical choice and its reasons. See [decisions/](decisions/). |
| Walking skeleton | The thinnest version of the app that works end to end (install, sign in, list, play), built first to prove the whole chain before adding features. |
| Refactoring | Changing how code is organized without changing what it does, to keep it readable and easy to maintain. |
