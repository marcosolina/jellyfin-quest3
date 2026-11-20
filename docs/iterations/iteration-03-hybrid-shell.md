# Iteration 3: milestone 2, hybrid shell

**Status:** Closed · **Started:** 2026-11-20 · **Closed:** 2026-11-21 · **Release:** 0.2.0

## Goal

Turn the app into a hybrid app ([ADR-003](../decisions/ADR-003-hybrid-app.md)): it opens as a normal Horizon OS window with the system bar, and switches to the immersive scene on request and back. No Jellyfin yet: this is the shell that the sign-in, browsing and cinema milestones will fill.

## Scope

| ID | Type | Title |
|---|---|---|
| JQ-028 | Story | The app opens as a Horizon OS window with the system bar |
| JQ-029 | Story | Enter the cinema from the window and come back |
| JQ-033 | Story | The Jellyfin logo as the app icon and in the README (added at release time, at Marco's request) |

## Acceptance criteria

**JQ-028 Window**

- Starting the app from the library opens a window with the system bar: app name, close button, and the other system controls (fullscreen, resize) that Horizon OS offers.
- The window shows what the milestone 1 panel showed: greeting, version and decoder list.
- Closing the window with the system close button quits the app.

**JQ-029 Cinema and back**

- An "Enter cinema" button in the window opens the immersive scene: passthrough with the floating panel of milestone 1, including the "Hide the room" button and the handle bar.
- The immersive panel has a "Back to window" button that closes the immersive scene and shows the window again, as it was.
- Pressing the system Meta button while in the cinema behaves like in other Quest apps.

## Out of scope

Jellyfin sign in, browsing, video: next milestones. Recentering inside the cinema (JQ-026) unless it comes for free.

## Test checklist (Marco, on the headset)

Tested by Marco on 2026-11-20 with the dev build. All passed.

- [x] The app opens from the library as a window with the system bar (name, close, minimise, fullscreen), showing greeting, version and decoder list.
- [x] "Enter cinema" opens the immersive scene with the control panel over the room.
- [x] "Back to window" closes the cinema and the window comes back as it was.
- [x] The Meta button in the cinema opens and closes the system menu over the cinema, as in other apps.
- [x] The close button in the window bar quits the app.
- [x] The Jellyfin logo is the app icon (JQ-033, added after the test). It is in the APK, but Horizon OS never shows it: the library, the taskbar and Settings show no icon or a "?" placeholder for every sideloaded app, Meta's own Mobile MRC Setup included. Accepted by Marco as a platform limit.

## Phase log

| Phase | Status | Notes |
|---|---|---|
| Research | Done | Meta's HybridSample: the window is a Compose activity with the `2D` and `LAUNCHER` categories; the cinema has the `VR` category and is started from the window; going back asks Horizon OS Home to reopen the window through a pending intent. Hotaru uses the same hand-off. Branch `iteration/03-hybrid-shell`. |
| Plan | Done | Approved by Marco on 2026-11-20. |
| Code | Done | New packages `window` (launcher window), `cinema` (immersive activity and its panel) and `ui` (shared theme); the milestone 1 activity is gone. |
| Build | Done | All checks pass. |
| Test | Done | See the checklist above. |
| Release | Done | Version 0.2.0 (version code 2), signed, [CHANGELOG](../../CHANGELOG.md), tag `v0.2.0`. |
| Deploy | Done | The signed 0.2.0 updated 0.1.0 in place on the Quest 3 (same key, no uninstall). |
| Review | Done | See below. |

## Code review

11 source files, 680 lines including tests; the largest file is `CinemaActivity` with 110 lines.

| Finding | Action |
|---|---|
| The strings `home_greeting` and `home_version` are still named after the removed `home` package | Task JQ-032 |
| The window and the cinema call each other's hand-off functions, a two-way dependency between the two packages | Fine with two screens; JQ-032 moves the hand-offs into a small `navigation` package when the next screen arrives |
| No new unit tests | By design: this iteration added only activities and screens, no logic |
| `docs/architecture.md` described the single-activity app | Updated |

## Retrospective

- Went well: Meta's sample and a recent open source project gave a working hybrid hand-off at the first try; the test passed without changes.
- Went less well: a build command chain reported success although the build had failed (the error code was lost in a pipe), so an old APK was copied and installed under the new version's name. Caught by checking the installed version; the wrong file was deleted. From now on every scripted build stops at the first failure (`set -o pipefail`), and the formatter runs after any scripted edit.
- Also noted: Marco added offline viewing as a goal (epic JQ-030, planned last).
