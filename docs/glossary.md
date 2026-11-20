# Glossary

Technical terms used in this project, in plain language. New terms are added the first time they appear in the research, the plans or the code.

## Quest platform

| Term | Meaning |
|---|---|
| Horizon OS | The operating system of the Meta Quest headsets. It is built on Android, so Quest apps are Android apps with extra VR capabilities. |
| APK | Android Package: the single file that contains an Android app. It is what gets installed on the Quest. |
| Sideloading | Installing an app from the PC instead of the Meta store. It is how this app will be installed. |
| Developer mode | A headset setting that allows sideloading and debugging. It is enabled from the Meta Horizon phone app, after registering a free Meta developer account. |
| ADB | Android Debug Bridge: a command line tool on the PC that talks to an Android device (the Quest) over USB or Wi-Fi, to install apps, read logs and take screenshots. |
| MQDH | Meta Quest Developer Hub: Meta's desktop app for working with a headset during development (install apps, see logs, mirror the view). |
| 2D panel app | An app that runs in a flat window inside the Quest home environment, like a phone or tablet app on a large floating screen. |
| Immersive app | An app that takes over the whole view and draws its own 3D world, for example a virtual cinema. |

## Video

| Term | Meaning |
|---|---|
| Codec | The method used to compress video or audio. Common video codecs: H.264 (AVC), H.265 (HEVC), VP9, AV1. Common audio codecs: AAC, AC-3 (Dolby Digital), E-AC-3, DTS, Opus. |
| Container | The file format that packs video, audio and subtitle tracks together: MKV, MP4, AVI. The container is not the codec: an MKV file can hold H.264 or H.265 video. |
| Encoding and decoding | Encoding compresses the pictures into a codec; decoding turns the compressed data back into pictures to show. A player only needs to decode. |
| Hardware decoding | Decoding done by a dedicated chip instead of the main processor. Fast, cool and light on the battery, but only for the codecs the chip supports. |
| Software decoding | Decoding done by the main processor. Works for more formats, but uses more power and may not keep up with high resolutions. |
| Bitrate | How much data each second of video uses. Higher bitrate means better quality and more network bandwidth. |
| HDR | High Dynamic Range (HDR10, HDR10+, Dolby Vision): video with a wider range of brightness and colour. It needs support in both the decoder and the display. |
| Tone mapping | Converting HDR video so that it looks right on a screen that does not support HDR. |
| Remux | Moving the tracks into a different container without re-encoding them. Fast and without quality loss. |
| 3D: SBS and OU | Stereoscopic video that packs the left eye and right eye pictures into one frame, either side by side (SBS) or over under (OU). |
| 180 and 360 video | Video that surrounds the viewer, half sphere (180) or full sphere (360). |

## Jellyfin

| Term | Meaning |
|---|---|
| Direct play | The client plays the original file as it is. Best quality, no work for the server. |
| Direct stream | The server remuxes the file on the fly into a container the client accepts, without re-encoding. |
| Transcoding | The server re-encodes video or audio into a format the client can play. Costs server processing power and some quality; used only when needed. |
| Device profile | The description a client sends to the server of what it can play (containers, codecs, resolutions, bitrates). The server uses it to choose between direct play, direct stream and transcoding. |
| Quick Connect | A Jellyfin way to sign in a new device by confirming a short code from a device that is already signed in, instead of typing the password. Useful in VR, where typing is awkward. |

## Project

| Term | Meaning |
|---|---|
| Iteration | One round of the cycle described in [process.md](process.md), with a single goal. |
| Spike | A research work item with a clear question; its answer is a file in [research/](research/). |
| ADR | Architecture Decision Record: a short document that records a significant technical choice and its reasons. See [decisions/](decisions/). |
| Walking skeleton | The thinnest version of the app that works end to end (install, sign in, list, play), built first to prove the whole chain before adding features. |
