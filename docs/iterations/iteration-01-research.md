# Iteration 1: research

**Status:** Closed · **Started:** 2026-11-20 · **Closed:** 2026-11-20

## Goal

Understand the platform and the video side well enough to choose the technology for a virtual cinema with 3D and 180/360 video, and to know what Marco needs to install for the first milestone.

## Scope

| ID | Title | Output | State |
|---|---|---|---|
| JQ-008 | Quest 3 platform basics | [research](../research/JQ-008-quest3-platform.md) | Closed |
| JQ-009 | Technology options | [research](../research/JQ-009-technology-options.md), [ADR-001](../decisions/ADR-001-technology-stack.md) | Closed |
| JQ-010 | Development environment | [research](../research/JQ-010-development-environment.md) | Closed |
| JQ-011 | Marco's server and media | [research](../research/JQ-011-server-and-media.md) | Closed |
| JQ-012 | Existing ways to watch Jellyfin on the Quest | [research](../research/JQ-012-existing-solutions.md) | Closed |
| JQ-018 | Video on the Quest 3 | [research](../research/JQ-018-video-on-quest3.md) | Closed |

JQ-015 (Jellyfin API) moved out: it is needed only before the sign-in milestone.

## Phase log

| Phase | Status | Notes |
|---|---|---|
| Research | Done | Three web research tracks in parallel (platform and tools, technology options, existing solutions), plus Marco's server, library and PC checked directly. |
| Plan | Done | ADR-001 proposed (Meta Spatial SDK with Kotlin). Milestone 1 planned in [iteration-02-hello-world.md](iteration-02-hello-world.md). |
| Code | Skipped | Research only. |
| Build | Skipped | |
| Test | Done | Marco approved the research, ADR-001 and the milestone 1 plan. |
| Release | Done | Research committed and pushed to `main`. |
| Deploy | Skipped | |
| Review | Done | See the retrospective. |

## Key findings

- The Platform SDK is not needed; developer mode and sideloading are.
- Meta Spatial SDK in Kotlin covers every cinema requirement with Meta's own video layers; two open source Jellyfin Quest clients (Hotaru, JellyQuest) already use the same stack.
- Marco's library plays mostly as is on the Quest 3; the app needs an FFmpeg audio decoder (AC-3 is 60% of the audio), subtitles on their own panel, and server tone mapping for HDR at first.
- There is no real 3D, 180 or 360 content on the server yet: test files will be needed.
- Jellyfin knows 3D formats from file names but has no field for 180 and 360.

## Acceptance checklist

- [x] Marco approves ADR-001.
- [x] Marco has the list of tools to install and the developer mode steps.
- [x] Marco approves the milestone 1 plan.

## Retrospective

- Went well: three research tracks in parallel, plus direct checks of the server and the PC, answered every open question in one iteration. Looking at real data (the library database, frames from the "3D" films) gave facts that documentation could not.
- To keep in mind: some claims from the web research are marked unconfirmed; milestone 1 checks the most important ones (decoders) on the headset itself.
- No code yet, so no code structure review.
