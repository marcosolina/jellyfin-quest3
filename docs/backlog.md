# Backlog

All work items, grouped by epic. States and types are defined in [process.md](process.md#work-items). The current iteration is shown on the [board](board.md).

**Next free ID: JQ-034**

Items without an iteration are not planned yet. Following the small milestones approach, they are scheduled one milestone at a time, after the previous one has been tested on the headset.

## Epics

| ID | Epic | Goal | State |
|---|---|---|---|
| JQ-001 | Foundation | Way of working, development environment, build, install on the headset, release pipeline. | Active |
| JQ-002 | Connect and sign in | Connect to a Jellyfin server, sign in comfortably in VR, remember the session. | New |
| JQ-003 | Browse | Libraries, posters, details, search, continue watching, next up. | New |
| JQ-004 | Playback | Smooth video playback, audio and subtitle tracks, watch progress synced with the server. | New |
| JQ-005 | Immersive viewing | Virtual cinema screen, 3D video (side by side, over under), 180 and 360 video, shown in the right mode for the format of each file. Core scope. | New |
| JQ-030 | Offline viewing | Download videos to the headset and watch them without the server (for example while travelling); watch progress synced when back online. Planned last. | New |

## JQ-001 Foundation

| ID | Type | Title | State | Iteration |
|---|---|---|---|---|
| JQ-006 | Task | Create the project structure: `CLAUDE.md`, `README.md`, `docs/` | Closed | 0 |
| JQ-007 | Task | Agree on the way of working ([process.md](process.md)) | Closed | 0 |
| JQ-008 | Spike | Quest 3 platform basics: Horizon OS, app types, packaging, developer mode, sideloading ([research](research/JQ-008-quest3-platform.md)) | Closed | 1 |
| JQ-009 | Spike | Technology options for a Quest 3 virtual cinema with 3D and 180/360 video ([research](research/JQ-009-technology-options.md), [ADR-001](decisions/ADR-001-technology-stack.md)) | Closed | 1 |
| JQ-010 | Spike | Development environment: tools to install, how Claude builds, installs, reads logs and takes screenshots ([research](research/JQ-010-development-environment.md)) | Closed | 1 |
| JQ-011 | Task | Collect Marco's setup: server, hardware, network, media formats ([research](research/JQ-011-server-and-media.md)) | Closed | 1 |
| JQ-012 | Spike | Existing ways to watch Jellyfin on the Quest; how formats are detected ([research](research/JQ-012-existing-solutions.md)) | Closed | 1 |
| JQ-023 | Task | Set up the development environment and connect the headset (Marco installs the tools and enables developer mode; Claude configures the PC and checks `adb`) | Closed | 2 |
| JQ-021 | Story | As Marco I want a hello world app running on my Quest 3, built with the chosen SDK and tools, so that we prove the build and install process | Closed | 2 |
| JQ-013 | Story | As Marco I want to install the app on the Quest from the PC with one command, so that every iteration can be deployed quickly | Closed | 2 |
| JQ-025 | Story | As a viewer I want to move a panel by grabbing its handle bar, and to find it in front of me when I put the headset on, so that I can place it comfortably (added during the milestone 1 test) | Closed | 2 |
| JQ-028 | Story | As a viewer I want the app to open as a normal Horizon OS window with the system bar (title, close, fullscreen), so that it behaves like other Quest apps ([ADR-003](decisions/ADR-003-hybrid-app.md)) | Closed | 3 |
| JQ-029 | Story | As a viewer I want to enter the cinema from the window and come back to it, so that I browse in the window and watch in the immersive space | Closed | 3 |
| JQ-026 | Story | As a viewer I want the Quest recenter (holding the Meta button) to bring the cinema in front of me, as in other Quest apps (the window gets it from the system, ADR-003) | New | |
| JQ-032 | Task | Tidy after the hybrid split: rename the `home_*` strings to `window_*`; move the window and cinema hand-offs into a `navigation` package when the next screen arrives (milestone 2 review) | New | 4 |
| JQ-027 | Task | Shrink the APK (88 MB): strip native libraries (install the NDK) and enable code shrinking with the keep rules Spatial SDK needs | New | |
| JQ-024 | Task | Add detekt static analysis once detekt 2.0 is stable ([ADR-002](decisions/ADR-002-code-quality-tooling.md)) | New | |
| JQ-033 | Story | As Marco I want the Jellyfin logo as the app icon and in the README (CC BY-SA 4.0, credited) | Closed | 3 |
| JQ-014 | Task | Decide the app name and the license (the repository is public; the stack mixes LGPL, GPL, MPL and Meta's closed SDK licenses, see JQ-009; the Jellyfin name and logo make the app look official, so a published version would need Jellyfin's view) | New | |

## JQ-002 Connect and sign in

| ID | Type | Title | State | Iteration |
|---|---|---|---|---|
| JQ-015 | Spike | Jellyfin API and Kotlin SDK with Jellyfin 12: authentication (including Quick Connect), browsing, playback info, progress reporting | New | 4 |
| JQ-016 | Story | As a viewer I want to sign in to my server from the headset, so that I can see my media | New | 4 |

## JQ-003 Browse

| ID | Type | Title | State | Iteration |
|---|---|---|---|---|
| JQ-017 | Story | As a viewer I want to see the movies in my library, so that I can pick one to watch | New | |

## JQ-004 Playback

| ID | Type | Title | State | Iteration |
|---|---|---|---|---|
| JQ-018 | Spike | Video on the Quest 3: decoders, HDR, audio, subtitles, projection detection ([research](research/JQ-018-video-on-quest3.md)) | Closed | 1 |
| JQ-022 | Spike | List the headset's video and audio decoders on the device, to confirm JQ-018 | Closed | 2 |
| JQ-019 | Story | As a viewer I want to play a movie, so that I can watch it on the headset | New | |
| JQ-020 | Story | As a viewer I want the server to remember where I stopped, so that I can resume later on any device | New | |

## JQ-005 Immersive viewing

No stories yet: they will be written when the immersive milestones get close. Known needs from the research: cinema screen, 3D and 180/360 modes, projection detection order ([JQ-012](research/JQ-012-existing-solutions.md#recommendation)), subtitles on their own panel, test files on the server.

## JQ-030 Offline viewing

Requested by Marco on 2026-11-20, to be done after the core epics. Known questions for the spike: Jellyfin's download permission and API (original file or a smaller converted copy), space on the 512 GB headset, downloads in the background, subtitles and audio tracks offline, and sending the watch progress to the server when back online.

| ID | Type | Title | State | Iteration |
|---|---|---|---|---|
| JQ-031 | Spike | Offline downloads: Jellyfin download API and permissions, original or converted copies, storage and background downloads on the Quest, syncing watch progress after offline viewing | New | |
