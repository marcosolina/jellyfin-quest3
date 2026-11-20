# JQ-012: Existing ways to watch Jellyfin on the Quest

Researched on 2026-11-20 with web searches, store listings, forums, GitHub and the Jellyfin source code (sources at the end). Claims marked *(unconfirmed)* could not be verified. Reddit could not be checked (blocked to the research tools); store prices come from a third-party mirror of the Meta store.

## Question

How do people watch Jellyfin on a Quest today, what is missing, what can we learn from, and how do players and Jellyfin know whether a video is flat, 3D, 180 or 360?

## Short answer

There is no official VR client. The official Android app runs as a flat window, and the web client in the Quest browser makes the viewer choose the projection by hand. Several **closed-source VR players** connect to Jellyfin (4XVR and Whirligig natively; CineVoid, Thru3D and Phyn are new in 2026), and a wave of **open source Quest clients** appeared in October and November 2026. The closest to this project, **Hotaru**, uses exactly the stack proposed in [ADR-001](../decisions/ADR-001-technology-stack.md). Jellyfin knows about **3D** (`Video3DFormat`), set mainly from file names, but has **no field for 180 or 360** yet: clients rely on file names, tags or metadata inside the file.

## Findings

### Official Jellyfin apps

| App | On Quest | Limits |
|---|---|---|
| Jellyfin for Android / Android TV | Sideloaded (no Meta store listing found), runs as a flat window | No 3D, 180 or 360. Handing a video to an external VR player broke with Horizon OS v77 to v79 *(unconfirmed whether fixed)* |
| Jellyfin web in the Quest browser | Works, with sync and subtitles | Projection chosen by hand in fullscreen; reports of stretched full SBS; a 2026 browser bug showed one eye only for 180 3D. A VR mode for jellyfin-web was proposed and closed unmerged (June 2026) |

Upstream 3D work in the official apps targets 3D TVs and displays, not VR.

### Third-party VR players

| App | Connection to Jellyfin | 3D / 180 / 360 | Price |
|---|---|---|---|
| 4XVR | Native Jellyfin, Plex, Emby; DLNA, SMB, WebDAV | Detects flat/180/360 and full/half SBS/OU; decodes Blu-ray 3D (MVC) | Free, premium €29.99 |
| Whirligig | Native Jellyfin and Plex; SMB | SBS/OU with depth controls; 180/360 | €4.99 |
| CineVoid (2026) | Jellyfin, Plex, Emby, DLNA, SMB | SBS/TB, 180/360 | €7.99 |
| Thru3D (early access, Nov 2026) | Jellyfin, Emby, Plex | SBS/TB, MV-HEVC, 180/360 | €2.99 |
| Phyn (early access, Sep 2026) | Jellyfin | Full and half SBS/OU, subtitles in 3D | Freemium |
| JellyTheatre (Oct 2026) | Jellyfin, as a web app | No 3D yet | Free, open source |
| Skybox, Moon, Pigasus, Bigscreen | DLNA or SMB only | Various | €0 to €9.99 |
| DeoVR, HereSphere | Through community Jellyfin server plugins | Filename markers | Free / €29.99 |

Watch progress sync with Jellyfin is not documented for most of them *(unconfirmed)*. DLNA from Jellyfin is a separate plugin, works only on the local network, and is described by users as "hit and miss".

### Open source Quest and XR clients

| Project | License | Stack | What it covers | Use for us |
|---|---|---|---|---|
| [Hotaru](https://github.com/Anthonyy232/Hotaru) | MPL 2.0 | Kotlin, Compose, Media3 1.11, Meta Spatial SDK 0.15, Jellyfin SDK 1.9.4 | SBS/TB, 180/360 from names in the path, progress, libass subtitles, direct play then remux then transcode; needs Jellyfin 12+ | **Closest reference.** Same stack. `QuestSceneHost.kt` (video layers) and `Rules.kt` (3D format to layout). Only 5 days old, one maintainer |
| [JellyQuest](https://github.com/JoeCotellese/JellyQuest) | MIT | Kotlin, Spatial SDK, ExoPlayer on a curved layer | Quick Connect, theatre presets; no 3D | Small Spatial SDK starter |
| [quest-jellyfin-3d](https://github.com/Josh-Archer/quest-jellyfin-3d) | **No license** (all rights reserved) | Rust, native OpenXR | Flat, curved, 180/360, fisheye, Quick Connect, progress | Ideas and detection tests only; **code must not be copied** |
| [JellyXR](https://github.com/Cuteminded/JellyXR) | Apache 2.0 | Godot 4.7 with an Android video plugin | 3D from `Video3DFormat` then file name; progress; picture subtitles | `scripts/core/stereo.gd` is a compact reference |
| [JellyTheatre](https://github.com/valentinp72/JellyTheatre) | GPL 3.0 | WebXR | No stereo yet | `docs/TECHNICAL.md`: device profile at runtime, `PlaybackInfo`, progress reporting |
| [jellyfin-plugin-deovr](https://github.com/hackeriori/jellyfin-plugin-deovr) | MIT | Server plugin (C#) | The most complete detection rules found | Detection priority order |

### What Quest users ask for

A native VR client; correct full SBS and over under without stretching; 180 and 360 support (a Jellyfin moderator said it needs a separate client); reliable detection ("3D_Full_SBS" is detected as half SBS). Upstream, Jellyfin pull request #18085 would add 180, 360, fisheye and MV-HEVC values to `Video3DFormat`, but it is waiting for version 13.

### How Jellyfin sets `Video3DFormat`

Values: `HalfSideBySide`, `FullSideBySide`, `HalfTopAndBottom`, `FullTopAndBottom`, `MVC`. Sources, in order:

1. **File path**, including folder names. Tokens are separated by `( ) - . _ [ ]` or spaces. Either `3d` followed by `hsbs`, `sbs`, `htab` or `tab`, or a bare `fsbs`, `hsbs`, `sbs`, `ftab`, `htab`, `tab`, `sbs3d`, `mvc`. Plain `sbs` and `tab` mean **half**, which is why "3D_Full_SBS" is read as half SBS: full width needs `fsbs` or `ftab`.
2. **NFO file**: `<format3d>` with HSBS, HTAB, FTAB, FSBS or MVC.
3. **Probing**: only the MKV stereo tag `left_right`, read as full side by side, and only if nothing else set the value.
4. **By hand** in the metadata editor.

Read from the API: `BaseItemDto.Video3DFormat`, `MediaSourceInfo.Video3DFormat` (per version, in `PlaybackInfo`), the filter `/Items?is3D=true`; `Path`, `Tags` and `MediaSources` must be requested explicitly.

Behaviours to handle:

- When a film has several versions, 3D versions are listed after the 2D ones: a VR client must pick the 3D version itself.
- When transcoding, Jellyfin crops a packed 3D frame to one eye if the client asks for both a width and a height: the client must not do that when it wants stereo.

### Conventions outside Jellyfin

| Source | Projection markers | Stereo markers |
|---|---|---|
| DeoVR | `_180`, `_360`, `_fisheye`, `_fisheye190`, `_mkx200` | `LR`, `3DH`, `SBS`; `TB`, `3DV`, `OverUnder` |
| HereSphere | `_180`, `_360`, `_F180`, `_MKX200`, `_EAC360` | `_LR`, `_RL`, `_TB`, `_BT`, `_3DH`, `_3DV` |
| Meta legacy | equirect assumed | `_TB`, `_BT`, `_LR`, `_RL` |
| Others | `180`, `360`, `_VR180`, `_VR360` | `sbs`, `lr`, `tb`, `ou`, `_MONO` |

Half or full width is usually inferred from the aspect ratio (a 16:9 film in full SBS is 32:9).

Metadata inside files: Matroska `StereoMode` (side by side, top bottom, but not half or full) and `Projection` (equirectangular, cubemap, mesh); MP4 Spherical Video V2 boxes `st3d` (stereo layout) and `sv3d` (projection, with crop bounds that make 180 video); Apple MV-HEVC. Media3 exposes them as `Format.stereoMode` and `Format.projectionData`, but ignores some rare values (MKV right-eye-first layouts, MP4 right-left).

## Recommendation

1. **Keep the stack in ADR-001**: two independent open source projects (Hotaru, JellyQuest) already build Jellyfin Quest clients on Spatial SDK, Media3 and the Jellyfin Kotlin SDK.
2. **Learn from** Hotaru (video layers and 3D rules; MPL 2.0 allows reusing its files if they keep their license), JellyTheatre's technical notes (device profile, playback, progress), and the DeoVR plugin's detection order. quest-jellyfin-3d has no license: read only.
3. **Projection detection order** for our app, to be confirmed in the immersive milestone: manual choice remembered per item, then Jellyfin tags (`VR180`, `VR360`), then `Video3DFormat`, then metadata inside the file, then file name markers, then flat.
4. **Naming test files**: use Jellyfin's tags so the server sets `Video3DFormat` (for example `Film (2010).3D.fsbs.mkv`) and `_180` or `_360` markers for VR video.
5. **Try before building**: Marco may want to try 4XVR (free) or Hotaru on the headset to see what he likes and dislikes; it sharpens the requirements for the immersive milestones.

## Open points

- Reddit discussions not checked.
- Progress sync in the closed-source players.
- Whether the external player bug of the official Android app is fixed.

## Sources

Checked 2026-11-20.

- Jellyfin source: `MediaBrowser.Model/Entities/Video3DFormat.cs`, `Emby.Naming/Video/Format3DParser.cs`, `Emby.Naming/Common/NamingOptions.cs`, `Emby.Server.Implementations/Library/Resolvers/BaseVideoResolver.cs`, `MediaBrowser.XbmcMetadata/Parsers/BaseNfoParser.cs`, `MediaBrowser.MediaEncoding/Probing/ProbeResultNormalizer.cs`, `MediaBrowser.Controller/MediaEncoding/EncodingHelper.cs`, in https://github.com/jellyfin/jellyfin
- Jellyfin pull requests: https://github.com/jellyfin/jellyfin/pull/18085, https://github.com/jellyfin/jellyfin/pull/18060, https://github.com/jellyfin/jellyfin-web/pull/7571
- Jellyfin docs: https://jellyfin.org/docs/general/server/media/movies, https://jellyfin.org/downloads/clients/
- Jellyfin forum: https://forum.jellyfin.org/t-watching-fsbs-in-vr, https://forum.jellyfin.org/t-360-degree-video-and-photos, https://forum.jellyfin.org/t-3d-full-sbs-video-detection-and-playback
- Meta forum: https://communityforums.atmeta.com/other-troubleshooting-14/v79-unable-to-launch-third-party-player-from-jellyfin-and-other-media-servers-171116
- Store listings (third-party mirror): https://queststoredb.com/app/4xvr-video-player/5936567899722707, https://queststoredb.com/app/whirligig-media-player/3581543561949869, https://queststoredb.com/app/cinevoid-vr-media-player-1199393973267430/, https://queststoredb.com/app/thru3d-media-player-xr-2531323740612319/, https://queststoredb.com/app/phyn-jellyfin-media-player-8075426475891543/, https://queststoredb.com/app/jellytheatre/4688003738150435
- Format conventions: https://deovr.com/app/doc, https://heresphere.itch.io/heresphere-vr-video-player-quest-2/devlog/397826/update-v07-released, https://github.com/google/spatial-media/blob/master/docs/spherical-video-v2-rfc.md, https://github.com/ietf-wg-cellar/matroska-specification/blob/master/ebml_matroska.xml
- Media3 source: `MatroskaExtractor.java`, `BoxParser.java` in https://github.com/androidx/media
- Open source projects: linked in the table above.
