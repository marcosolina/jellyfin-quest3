# JQ-011: Marco's server and media

Collected on 2026-11-20 from the server's public info endpoint, its Docker configuration, a read-only query of the Jellyfin database, and `ffprobe` on a few files. Addresses and credentials are not in this file (the repository is public).

## Question

What does the app connect to, and what kind of files will it have to play?

## Short answer

Jellyfin **12.4** in Docker on a home mini PC, reachable on the home network and from the internet over HTTPS, under the path `/jellyfin`. The library is about **5,500 videos**, mostly H.264 and HEVC in MKV and MP4, with 517 in 4K and 508 in HDR (384 of them Dolby Vision profile 8). **AC-3 is the most common audio codec** and almost 40% of the subtitle tracks are **pictures (DVD and PGS)** rather than text. There is **no 3D, 180 or 360 content yet**: test files will be needed.

## Findings

### Server

| Item | Value |
|---|---|
| Jellyfin version | 12.4.0 |
| Runs on | Docker, Ubuntu 24.04, AMD Ryzen 5 5600U with Radeon Vega graphics, 62 GB RAM |
| Address | Base path `/jellyfin`; HTTP on the home network, HTTPS from the internet through a reverse proxy |
| Server discovery | Not available: the UDP discovery port is not published by Docker, so the app must let the user enter the server address |
| Hardware transcoding | VA-API on the Radeon graphics. Decodes H.264, HEVC (8 and 10 bit), VP9, MPEG-2, VC-1; **encodes H.264 only** |
| Tone mapping | On (HDR converted to SDR when transcoding) |
| Measured | A 4K HEVC HDR film transcoded to 1080p at 3 Mbps ran at about 100 frames per second, 4 times faster than real time |

### Library

1,740 movies, 3,742 episodes in 30 series, in five libraries (Movies, TV Series, Anime Series, Cartoons, Documentaries).

| Containers | Files |
|---|---|
| MKV | 3,053 |
| MP4 / M4V | 2,361 |
| AVI | 69 |

| Video codec | Streams | Share |
|---|---|---|
| H.264 8 bit (Main, High, Baseline) | 3,747 | 68.3% |
| H.264 10 bit (High 10) | 18 | 0.3% |
| HEVC 8 bit | 895 | 16.3% |
| HEVC 10 bit (Main 10) | 685 | 12.5% |
| MPEG-4 Part 2 (Xvid, DivX) and older | 138 | 2.5% |

| Resolution | Streams |
|---|---|
| SD (below 1200 pixels wide) | 2,831 |
| 720p | 486 |
| 1080p | 1,649 |
| 4K | 517 |

| HDR | Streams |
|---|---|
| HDR (PQ transfer: HDR10 or Dolby Vision) | 508 |
| of which Dolby Vision profile 8 (has an HDR10 compatible base) | 384 |
| of which Dolby Vision profile 7 (dual layer) | 3 |

| Audio codec | Tracks | Share |
|---|---|---|
| AC-3 (Dolby Digital) | 5,451 | 60.6% |
| AAC | 2,952 | 32.8% |
| E-AC-3 (Dolby Digital Plus) | 208 | 2.3% |
| DTS | 173 | 1.9% |
| MP3 | 134 | 1.5% |
| FLAC | 80 | 0.9% |
| TrueHD | 2 | 0.0% |

| Subtitle format | Tracks | Kind |
|---|---|---|
| SRT (SubRip), 41 of them external files | 4,705 | Text |
| DVD subtitles (VobSub) | 2,297 | Pictures |
| PGS (Blu-ray) | 1,813 | Pictures |
| ASS / SSA (styled, common in anime) | 1,708 | Text with styling |
| MP4 text and WebVTT | 37 | Text |

### 3D, 180 and 360 content

Four movies have "3D" in the title. Their resolution, the absence of stereo metadata and a frame extracted from two of them show they are ordinary 2D versions. No file has Jellyfin's 3D format set and no file looks like 180 or 360 video.

## Implications for the app

1. **Video:** H.264 and HEVC cover 97% of the library. If the Quest 3 decodes both in hardware, including HEVC 10 bit, almost everything can be played as is (direct play). H.264 High 10 (18 files) and MPEG-4 Part 2 (138 files) usually have no hardware decoder on Android devices: the server can transcode them. To verify in JQ-018.
2. **Audio:** AC-3 alone is 60% of the audio tracks, plus E-AC-3, DTS and TrueHD. The headset has no surround receiver to pass audio to, so these must be decoded somewhere: in the app (the official Jellyfin Android apps add an FFmpeg audio decoder for this) or by the server converting only the audio, which is cheap. This choice must be made early.
3. **HDR:** 508 files are HDR. Whether the Quest 3 display can show HDR, and who tone maps (the headset or the server), is a question for JQ-018.
4. **Subtitles:** picture subtitles (DVD and PGS, about 4,100 tracks) need a player that can draw them; otherwise the server burns them into the picture, which forces a full video transcode. ASS styling matters for the anime library.
5. **Immersive features need test files:** a small test library with 3D (side by side, over under) and 180/360 samples, named with Jellyfin's 3D filename tags, will be needed before the immersive milestones.
6. **Jellyfin 12:** the API client must support Jellyfin 12.x. To verify in JQ-015 before the sign-in milestone.

## Open points

- Confirm the Quest 3 hardware decoders against this list (JQ-018).
- Choose where audio and picture subtitles are handled (app or server), once the technology is chosen.
- Marco to add test 3D and 180/360 files when the immersive milestones get close.

## Sources

- Server public info endpoint `/System/Info/Public`, checked 2026-11-20.
- Marco's Docker project for Jellyfin (`compose.yml`, `README.md`), read 2026-11-20.
- Jellyfin database, read-only query of `BaseItems` and `MediaStreamInfos`, 2026-11-20.
