# Backlog

All work items, grouped by epic. States and types are defined in [process.md](process.md#work-items). The current iteration is shown on the [board](board.md).

**Next free ID: JQ-022**

Items without an iteration are not planned yet. Following the small milestones approach, they are scheduled one milestone at a time, after the previous one has been tested on the headset.

## Epics

| ID | Epic | Goal | State |
|---|---|---|---|
| JQ-001 | Foundation | Way of working, development environment, build, install on the headset, release pipeline. | Active |
| JQ-002 | Connect and sign in | Connect to a Jellyfin server, sign in comfortably in VR, remember the session. | New |
| JQ-003 | Browse | Libraries, posters, details, search, continue watching, next up. | New |
| JQ-004 | Playback | Smooth video playback, audio and subtitle tracks, watch progress synced with the server. | New |
| JQ-005 | Immersive viewing | Virtual cinema screen, 3D video (side by side, over under), 180 and 360 video, shown in the right mode for the format of each file. Core scope. | New |

## JQ-001 Foundation

| ID | Type | Title | State | Iteration |
|---|---|---|---|---|
| JQ-006 | Task | Create the project structure: `CLAUDE.md`, `README.md`, `docs/` | Closed | 0 |
| JQ-007 | Task | Agree on the way of working ([process.md](process.md)) | Closed | 0 |
| JQ-008 | Spike | Quest 3 platform basics: Horizon OS, app types (2D panel and immersive), packaging, developer mode, sideloading | Active | 1 |
| JQ-009 | Spike | Technology options for a Quest 3 virtual cinema with 3D and 180/360 video; comparison and recommendation (outcome: ADR-001) | Active | 1 |
| JQ-010 | Spike | Development environment: IDE and tools to install on the PC; how Claude can build, install, read logs and take screenshots on the headset | Active | 1 |
| JQ-011 | Task | Collect Marco's setup: Jellyfin server version, OS and hardware, network, typical media (codecs, 4K, HDR, 3D files) | Active | 1 |
| JQ-012 | Spike | Existing ways to watch Jellyfin on the Quest today: what works, what is missing, what to learn from | Active | 1 |
| JQ-021 | Story | As Marco I want a hello world app running on my Quest 3, built with the chosen SDK and tools, so that we prove the build and install process | New | 2 |
| JQ-013 | Story | As Marco I want to install the app on the Quest from the PC with one command, so that every iteration can be deployed quickly | New | 2 |
| JQ-014 | Task | Decide the app name and the license (the repository is public) | New | |

## JQ-002 Connect and sign in

| ID | Type | Title | State | Iteration |
|---|---|---|---|---|
| JQ-015 | Spike | Jellyfin API and official SDKs: authentication (including Quick Connect), browsing, playback, progress reporting, supported server versions | New | |
| JQ-016 | Story | As a viewer I want to sign in to my server from the headset, so that I can see my media | New | |

## JQ-003 Browse

| ID | Type | Title | State | Iteration |
|---|---|---|---|---|
| JQ-017 | Story | As a viewer I want to see the movies in my library, so that I can pick one to watch | New | |

## JQ-004 Playback

| ID | Type | Title | State | Iteration |
|---|---|---|---|---|
| JQ-018 | Spike | Video on the Quest 3: hardware decoders (codecs, resolutions, HDR), available player libraries, how Jellyfin chooses between direct play and transcoding | Active | 1 |
| JQ-019 | Story | As a viewer I want to play a movie, so that I can watch it on the headset | New | |
| JQ-020 | Story | As a viewer I want the server to remember where I stopped, so that I can resume later on any device | New | |

## JQ-005 Immersive viewing

No items yet: stories will be written after the technology choice (JQ-009).
