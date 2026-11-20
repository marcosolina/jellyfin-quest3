# Board

Last updated: 2026-11-20

## Current iteration

**Iteration 3: milestone 2, hybrid shell** · Phase: Plan (Marco confirms the details)

Goal: the app opens as a normal Horizon OS window with the system bar, and switches to the immersive scene and back ([ADR-003](decisions/ADR-003-hybrid-app.md)). Details in [iteration-03-hybrid-shell.md](iterations/iteration-03-hybrid-shell.md).

| ID | Type | Title | State |
|---|---|---|---|
| JQ-028 | Story | The app opens as a Horizon OS window with the system bar | New |
| JQ-029 | Story | Enter the cinema from the window and come back | New |

## Waiting on Marco

- Confirm the [milestone 2 plan](iterations/iteration-03-hybrid-shell.md).
- Approve or discuss [ADR-002](decisions/ADR-002-code-quality-tooling.md) (code quality tooling).
- Back up `private/release.keystore` and `private/signing.properties` somewhere safe: without them, a new release cannot update the installed app.

## Latest release

**0.1.0** (2026-11-20): hello world on the Quest 3, installed on Marco's headset. See the [changelog](../CHANGELOG.md).

## Iterations

| # | Goal | Status |
|---|---|---|
| 0 | Project setup: structure and way of working | Closed |
| 1 | Research: platform, technology, video, development environment | Closed |
| 2 | Milestone 1: hello world on the Quest 3 | Closed, released 0.1.0 |
| 3 | Milestone 2: hybrid shell, window with system bar plus immersive scene | Planned |
| 4+ | Next milestones, defined one at a time after each test on the headset | |
