# jellyfin-quest3

A native Meta Quest 3 app that connects to Marco's Jellyfin media server: a virtual cinema with 3D and 180/360 video, shown in the right mode for each file's format. Personal project: Claude is the developer, Marco is the product owner and the tester on the headset.

The repository is public: never commit server addresses, IPs, usernames, tokens or passwords. Those go in `private/` (git-ignored); Marco's server access details are in `private/server.md`.

Marco is new to Quest and Android development and to video encoding and decoding: explain technical terms plainly the first time they come up, and add them to the glossary.

## Project files

| What | Where |
|---|---|
| Project presentation | `README.md` |
| Way of working: roles, iteration cycle, work items, definitions of ready and done, Git rules | `docs/process.md` |
| Dashboard: current iteration and phase, items in progress, what waits on Marco | `docs/board.md` |
| Backlog: every epic, story, spike, task and bug, with state and next free ID | `docs/backlog.md` |
| Iteration logs: goal, scope, phase log, test checklist, retrospective | `docs/iterations/` |
| Research results, one file per spike, with index and format | `docs/research/` |
| Architecture decision records, with index and format | `docs/decisions/` |
| Glossary of technical terms | `docs/glossary.md` |

## Current state

- Live status: `docs/board.md`.
- Technology stack: Meta Spatial SDK with Kotlin, Media3, Jellyfin Kotlin SDK, Compose (`docs/decisions/ADR-001-technology-stack.md`, accepted 2026-11-20).
- Build, run and deploy commands: added here once the stack is chosen.

## Rules

- Update the board, the backlog and the iteration file in the same change as the work they describe.
- Keep this file short: a line or two per topic, details live under `docs/`. One topic per file; link instead of copying.
- Every significant technical choice gets an ADR, written so a non-expert can follow it.
- No application code before Marco approves the iteration plan.
- Keep the code well organized and readable; end every iteration with a code structure review whose refactoring needs become backlog tasks (`docs/process.md`, Code quality).
- No commit, push, tag or release without Marco's go-ahead.
- Markdown: one line per paragraph (no hard wrap); no spaced dashes in prose.
