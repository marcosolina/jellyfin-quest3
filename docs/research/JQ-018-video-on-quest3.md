# JQ-018: Video on the Quest 3

Researched on 2026-11-20 from Meta's media documentation, Android Media3 documentation and source code, and the Jellyfin repositories (sources at the end). Claims marked *(unconfirmed)* need a check on the headset. Compared with Marco's library in [JQ-011](JQ-011-server-and-media.md).

## Question

What can the Quest 3 play by itself, what needs the server's help, and how does the app know which projection (flat, 3D, 180, 360) a video needs?

## Short answer

The Quest 3 decodes **H.264, HEVC, VP9 and AV1 in hardware**, up to **8192x4096 at 60 fps**; 8K only with HEVC and AV1. That covers about 97% of Marco's library as is. **Audio** has no hardware path to a receiver: AC-3, E-AC-3, DTS and TrueHD are decoded in software by an FFmpeg extension in the app. The display is **SDR**, so **HDR** needs tone mapping, most simply by the server. **Picture subtitles** (DVD, PGS) and **ASS** are supported by the player library, but in VR they must be drawn on their own panel. The projection comes from Jellyfin's `Video3DFormat` field, from metadata inside the file, or from filename tags.

## Findings

### Video decoders

| Codec | Hardware decoding on Quest 3 | Marco's library |
|---|---|---|
| H.264 8 bit | Yes; treat resolutions above 4K as unsupported | 3,747 streams |
| H.264 10 bit (High 10) | Generally no hardware decoder on Android devices *(unconfirmed on Quest)* | 18 streams: server transcode |
| HEVC 8 and 10 bit | Yes, up to 8K; 10 bit implied by Meta's HDR support list *(confirm on headset)* | 1,580 streams |
| VP9 | Yes | none |
| AV1 | Yes (new on Quest 3; not on Quest 2 or Pro) | none |
| MPEG-4 Part 2, MPEG-2, older | Not listed by Meta | 138 streams: server transcode |

Meta gives 8192x4096 at 60 fps as the hardware limit, and realistic targets of 7680x3840 at 60 fps for 180 and 360 video, at 35 to 50 Mbps or more. Wi-Fi throughput is the other practical limit for 8K.

The first milestone app will list the decoders the headset reports (`MediaCodecInfo`), to confirm this table.

### HDR

- The Quest 3 has LCD panels without a documented HDR mode: treat it as an **SDR display**.
- What the system does with HDR10 video sent directly to a compositor layer is not documented *(test on headset)*.
- Simplest safe path: the app tells Jellyfin it cannot play HDR, and the server tone maps while transcoding. Marco's server does this on its GPU (a 4K HDR film to 1080p ran 4 times faster than real time), at the cost of resolution and some quality for the 508 HDR files.
- Dolby Vision profile 8 (384 files) carries an HDR10 compatible base, so it follows the same path. Profile 7 (3 files) needs server transcoding.

### Audio

The headset has no surround receiver to pass audio to, so every audio track is decoded on the headset or by the server.

| Codec | Decoded by | Marco's library |
|---|---|---|
| AAC, MP3, FLAC, Opus | Android, built in | 3,166 tracks |
| AC-3, E-AC-3 | FFmpeg extension in the app (Android's own decoders may exist, *unconfirmed*) | 5,659 tracks |
| DTS, TrueHD | FFmpeg extension in the app | 175 tracks |

The FFmpeg extension (`org.jellyfin.media3:media3-ffmpeg-decoder`) is what the official Jellyfin Android apps use. Surround tracks are mixed down for the headset speakers or headphones; Dolby Atmos objects are lost.

### Containers and subtitles

The Media3 player (ExoPlayer) handles MKV, MP4 and HLS (the format Jellyfin uses when it transcodes). AVI is not supported: the server remuxes it.

| Subtitles | Media3 support | Notes |
|---|---|---|
| SRT, WebVTT, MP4 text | Yes | Text |
| ASS / SSA | Basic styling | Full styling through libass (`ass-media`), as in the Jellyfin apps |
| PGS, DVD (VobSub) | Yes, as pictures | |

In the cinema, the video goes straight to the compositor, so subtitles cannot be drawn on top of it by the player. They will be drawn on a separate panel in front of the screen, fed by the player's subtitle events. For 3D video they must appear in both eyes at a comfortable depth.

### How the app knows the projection

| Source | What it tells | Notes |
|---|---|---|
| Jellyfin `Video3DFormat` | `HalfSideBySide`, `FullSideBySide`, `HalfTopAndBottom`, `FullTopAndBottom`, `MVC` | Set by Jellyfin from filename tags such as `.3d.hsbs.` (details in JQ-012). No field for 180 or 360 |
| Metadata inside the file | Stereo layout and spherical projection | Matroska `StereoMode` and `ProjectionType`, MP4 spherical boxes; Media3 exposes them as `Format.stereoMode` and `projectionData` |
| Filename tags | Conventions used by VR players, such as `_180`, `_360`, `_SBS`, `_TB` | Fallback, details in JQ-012 |
| Manual choice | The viewer picks the mode in the player | Always needed as an override |

MVC (Blu-ray 3D) cannot be decoded as stereo on Android *(unconfirmed)*: such files would play as 2D.

## Recommendation

- Direct play H.264 and HEVC; let the server transcode H.264 10 bit, MPEG-4 Part 2, MPEG-2, AVI and Dolby Vision profile 7.
- Bundle the FFmpeg audio decoder so the server never transcodes audio only because of AC-3, DTS or TrueHD.
- Start by declaring no HDR support in the device profile (server tone mapping); test direct HDR on the headset later.
- Subtitles on a separate panel; libass for ASS.
- Detect the projection from Jellyfin, then file metadata, then filename tags, with a manual override in the player.

## Open points

- Confirm on the headset: HEVC 10 bit, H.264 High 10, AC-3 and E-AC-3 decoders, maximum resolutions (milestone 1 decoder list).
- What happens with HDR10 sent directly to a layer.
- Refresh rate matching for 24 fps films (72 Hz) through Spatial SDK.

## Sources

Checked 2026-11-20.

- Meta media requirements: https://developers.meta.com/horizon/documentation/android-apps/media-requirements/
- Meta browser video (codec and 8K notes): https://developers.meta.com/horizon/documentation/web/browser-video/
- Media3 supported formats: https://developer.android.com/media/media3/exoplayer/supported-formats
- Media3 source (`MatroskaExtractor`, `Format`): https://github.com/androidx/media
- Media3 tone mapping (Transformer only): https://developer.android.com/media/media3/transformer/tone-mapping
- Jellyfin FFmpeg decoder for Media3: https://github.com/jellyfin/jellyfin-androidx-media
- Jellyfin `Video3DFormat`: https://github.com/jellyfin/jellyfin (`MediaBrowser.Model/Entities/Video3DFormat.cs`)
- libass for Media3: https://github.com/peerless2012/libass-android
- DeoVR guide to 8K on Quest 3 (vendor): https://deovr.com/blog/202-guide-uploading-vr-content-for-meta-quest-3-and-quest-3s
- Quest 3 display: https://en.wikipedia.org/wiki/Meta_Quest_3
