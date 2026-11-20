# ADR-003: Hybrid app, a window for browsing and an immersive cinema for watching

**Status:** Accepted (2026-11-20)

## Context

Milestone 1 made the app fully immersive: it takes over the whole view and draws its own panel. Testing it, Marco asked for the bar that other Quest apps have under their window: a thick bar with the app name and a close button (plus fullscreen and other controls), which appears on hover. That bar is drawn by Horizon OS itself, and only around **2D window apps** (normal Android activities shown as windows). An immersive app gets nothing from the system: Spatial SDK offers only a thin grab handle.

The app has two kinds of use: browsing the library (sign in, posters, details, search), which is ordinary 2D interface work, and watching, which needs the immersive cinema (screen, 3D, 180 and 360 video).

## Options

1. **Fully immersive, with our own window bar.** Draw a bar with title and close button under our panels. Doable, but it imitates the system and will never quite match it (fullscreen, resizing, recenter, future system changes), and every system feature must be rebuilt by hand.
2. **Hybrid app.** The app opens as a normal Horizon OS window, with the real system bar (title, close, fullscreen, resize), system placement and recenter, and the system keyboard. It switches to an immersive activity only to watch a video, and returns to the window afterwards. This is the pattern of Meta's HybridSample and of the Hotaru project ([JQ-012](../research/JQ-012-existing-solutions.md)).

## Decision

**Hybrid app.** A 2D window activity is the launcher and hosts all browsing screens; an immersive Spatial SDK activity hosts the cinema and is started from the window.

## Consequences

- The system bar, window placement, recentering and the system keyboard come for free in the window (this also answers most of JQ-026 for the browsing part).
- Two activities: state shared between them (the item to play, the session) must live outside the activities, in an application-level object.
- The switch between window and cinema must be smooth, and going back must return to the same place in the window.
- The window content is plain Jetpack Compose and can be tested without the immersive part.

## References

- Marco's request during the milestone 1 test, recorded in [iteration-02-hello-world.md](../iterations/iteration-02-hello-world.md).
- Meta HybridSample: https://github.com/meta-quest/Meta-Spatial-SDK-Samples (HybridSample)
- [JQ-012 Existing solutions](../research/JQ-012-existing-solutions.md) (Hotaru uses the same pattern)
