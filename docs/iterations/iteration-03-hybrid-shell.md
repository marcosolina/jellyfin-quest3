# Iteration 3: milestone 2, hybrid shell

**Status:** Planned (plan awaiting Marco's confirmation of the details)

## Goal

Turn the app into a hybrid app ([ADR-003](../decisions/ADR-003-hybrid-app.md)): it opens as a normal Horizon OS window with the system bar, and switches to the immersive scene on request and back. No Jellyfin yet: this is the shell that the sign-in, browsing and cinema milestones will fill.

## Scope

| ID | Type | Title |
|---|---|---|
| JQ-028 | Story | The app opens as a Horizon OS window with the system bar |
| JQ-029 | Story | Enter the cinema from the window and come back |

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

To be completed in the Test phase.

## Phase log

| Phase | Status | Notes |
|---|---|---|
| Research | | Meta's HybridSample and the Hotaru hybrid setup (manifest categories, the hand-off between window and immersive activity). |
| Plan | Active | Direction approved by Marco on 2026-11-20 (option B); details in this file to confirm. |
| Code | | |
| Build | | |
| Test | | |
| Release | | Version 0.2.0. |
| Deploy | | |
| Review | | |
