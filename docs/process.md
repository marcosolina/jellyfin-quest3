# Way of working

How this project is run: who does what, the iteration cycle, work items, and the rules that keep the repository and the documentation in order.

## Roles

| Who | Role | Responsibilities |
|---|---|---|
| Marco | Product owner, tester, user | Decides what the app should do and in which order, approves each iteration plan, tests on the Quest 3, reports bugs and feedback, gives the go-ahead for commits and releases. |
| Claude | Developer | Researches, proposes plans and decisions, writes code and tests, builds, prepares releases, keeps the documentation and the board up to date. |

## Iteration cycle

The work moves in iterations. Each iteration has one clear goal and goes through the phases below in order. A phase with nothing to do (for example, coding in a research iteration) is marked as skipped in the iteration file.

| Phase | What happens | Output | Exit gate |
|---|---|---|---|
| 1. Research | Investigate what the iteration touches and is still unknown: platform, APIs, libraries, video formats. | Files in [research/](research/), new terms in the [glossary](glossary.md). | The open questions are answered, or turned into spikes for a later iteration. |
| 2. Plan | Choose the stories for the iteration, write acceptance criteria, split them into tasks, record significant choices as ADRs. | Iteration file in [iterations/](iterations/), ADRs in [decisions/](decisions/). | **Marco approves the plan.** No code is written before this. |
| 3. Code | Implement the stories, with automated tests where they make sense. | Source code and tests on the iteration branch. | Code compiles, automated tests pass. |
| 4. Build | Produce the installable app with a single, repeatable command (later also in CI). | APK file. | Clean build from a fresh clone works. |
| 5. Test | Automated tests, then Marco tests on the headset following the checklist in the iteration file. | Test results in the iteration file, bugs in the backlog. | **Marco accepts the stories.** |
| 6. Release | Set the version number, update the changelog, tag the commit, produce the signed APK. | Git tag `vX.Y.Z`, `CHANGELOG.md` entry, release APK. | Release tag pushed (with Marco's go-ahead). |
| 7. Deploy | Install the release on the Quest 3 (over USB or Wi-Fi). | App updated on the headset. | App starts and reaches the Jellyfin server. |
| 8. Review | Marco uses the app day to day; feedback and bugs go into the backlog. Claude reviews the structure and readability of the code (see [Code quality](#code-quality)). Short retrospective on how the iteration went. | Retrospective and code review notes in the iteration file; new backlog items, including refactoring tasks. | Goal of the next iteration agreed, refactoring tasks prioritized. |

Phase 8 is an addition to the original list: it closes the loop (the "operate and monitor" part of DevOps) so that real use and a look at the code's health feed the next iteration.

### Iteration rules

- Iterations are sized by goal, not by calendar: small enough to finish and deploy in a few working sessions.
- The first coding iterations build a **walking skeleton**: the thinnest app that goes through the whole cycle (build, install on the Quest, sign in, list a library, play one video). It proves the toolchain and the riskiest parts before features are added.
- The walking skeleton is built in **small milestones**, each tested on the headset before the next one is defined. The first is a hello world app that proves the build and install process.
- Every coding iteration ends with a working app on the headset. If something cannot be finished, it moves back to the backlog instead of stretching the iteration.
- Each iteration has its own file: `iterations/iteration-NN-short-name.md`.

## Work items

All work is tracked in the [backlog](backlog.md), in the style of Azure DevOps or Jira. The [board](board.md) is the dashboard view of the current iteration.

### IDs

Every item gets an ID from a single sequence: `JQ-001`, `JQ-002`, and so on (JQ = Jellyfin Quest). IDs are never reused; the next free ID is noted at the top of the backlog. Commits, branches, research files and ADRs reference these IDs.

### Types

| Type | Use |
|---|---|
| Epic | A large capability, roughly the theme of one or more releases (for example "Video playback"). |
| Story | A user-visible slice that fits in one iteration, written as "As a viewer I want ..., so that ...", with acceptance criteria. |
| Spike | Research with a clear question. Its output is a file in [research/](research/). |
| Task | Technical work with no direct user value (tooling, setup, refactoring). |
| Bug | Something that does not behave as it was accepted. Includes steps to reproduce. |

### States

The states follow the Azure DevOps Agile process.

| State | Meaning |
|---|---|
| New | Captured, not yet ready to be worked on. |
| Active | Being worked on in the current iteration. |
| Resolved | Done by Claude, waiting for Marco's test or review. |
| Closed | Accepted by Marco. |
| Removed | Dropped; kept in the backlog for history with the reason. |

An item that cannot progress is marked **Blocked** in the board, with what it is waiting for.

### Definition of Ready (story)

- Acceptance criteria are written and Marco agrees with them.
- Dependencies and unknowns are resolved, or a spike exists for them.
- It fits in one iteration.

### Definition of Done (story)

- Acceptance criteria are met and verified on the Quest 3 by Marco.
- Automated tests cover the logic that can be tested off the headset.
- The app builds cleanly from a fresh clone.
- The code follows the rules in [Code quality](#code-quality).
- Backlog, board, iteration file and (if needed) glossary, ADRs and architecture description are updated.

## Code quality

The code must stay well organized and easy to read and maintain. This is a goal of the project in its own right, not something to fix at the end.

### Rules while coding

- Organize the code by feature (sign in, browse, player, cinema), with clear and consistent names, so that the folder tree tells what the app does.
- Small files, classes and functions, each with one job.
- Formatting and static checks run as part of the build, with tools chosen together with the technology (ADR-001). A build with violations is not done.
- Comments explain why something is done, not what the code does.
- No dead code and no commented-out code: Git keeps the history.
- Refactoring and feature changes go in separate commits.
- `docs/architecture.md` describes the code structure and its main components. It is created with the first code and kept current.

### Code review at the end of each iteration

In phase 8, Claude reviews the whole project structure and the code changed in the iteration, looking for:

- files, classes or functions that grew too large or do too many things;
- duplicated logic;
- unclear names, or code in the wrong place for its feature;
- logic without tests that could be tested off the headset;
- outdated dependencies or deprecated APIs;
- a gap between the code and `docs/architecture.md`.

The findings are written in the iteration file. Refactoring worth doing becomes Task items in the backlog and is planned in the next iteration like any other work: it changes code, so it goes through build, test and release too.

## Documentation rules

- `CLAUDE.md` in the root is the short index: a line or two per topic, linking to the details here.
- One topic per file. Link to other files instead of copying their content.
- Research files and ADRs follow the formats described in [research/README.md](research/README.md) and [decisions/README.md](decisions/README.md).
- Every significant technical choice is an ADR, written so that someone new to the topic can follow the reasoning.
- New technical terms go into the [glossary](glossary.md) the first time they appear.
- Markdown style: one line per paragraph (no hard wrap); no spaced dashes in prose.

## Source control and versions

- Repository: `github.com/marcosolina/jellyfin-quest3`, default branch `main`.
- `main` always holds a version that builds. Each iteration works on its own branch, `iteration/NN-short-name`, merged into `main` at release.
- Commit messages start with the work item ID: `JQ-012 Add server sign-in screen`.
- Versions follow semantic versioning, starting at `0.1.0` for the walking skeleton and staying below `1.0.0` until the app is good enough for daily use.
- `CHANGELOG.md` in the root is created with the first release.
- Claude does not commit, push, tag or publish without Marco's go-ahead, with one standing exception agreed on 2026-11-21: once Marco accepts a milestone on the headset, Claude releases it (commit, merge into `main`, tag, push) without asking again.

## Environments

| Environment | Details |
|---|---|
| Development PC | Windows 11 Pro. Tools to install are decided in iteration 1 (see the research on the development environment). |
| Headset | Meta Quest 3, 512 GB. Needs developer mode to install apps from the PC. |
| Media server | Marco's Jellyfin server. Version, hardware and network setup to be collected in iteration 1. |
