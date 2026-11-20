# Iteration 4: milestone 3, sign in to Jellyfin

**Status:** Planned (plan awaiting Marco's approval)

## Goal

The window connects to a Jellyfin server and signs in, and proves the session works by showing the user's libraries. First step of the walking skeleton's Jellyfin part (sign in, list, play).

## Scope

| ID | Type | Title |
|---|---|---|
| JQ-015 | Spike | Jellyfin API and Kotlin SDK with Jellyfin 12: server check, Quick Connect, username and password, where to keep the access token, HTTPS and home network addresses |
| JQ-016 | Story | Sign in to the server from the headset |
| JQ-032 | Task | Tidy after the hybrid split (milestone 2 review) |

## Acceptance criteria

**JQ-016 Sign in**

- The window asks for the server address, typed with the system keyboard. The app checks it and shows the server's name and version, or a clear message when the address is wrong, unreachable or not a Jellyfin server.
- Sign in works with **Quick Connect** (the window shows a code, approved from the phone or the browser, no password typed in VR) and with **username and password**.
- After signing in, the window shows "Signed in as *user* on *server*" and the names of the user's libraries.
- The session is remembered: closing and reopening the app keeps the user signed in. A "Sign out" button ends the session on the server too.
- The access token is stored encrypted on the headset, never written to the log, and excluded from backups.
- The Jellyfin access code lives in its own plain Kotlin module with unit tests (for example, turning what the user types into a valid server address), as planned in [architecture.md](../architecture.md#next-steps).

**JQ-032 Tidy**

- The `home_*` strings are renamed; the hand-offs between window and cinema sit in one place.

## Prerequisites (Marco)

- Quick Connect enabled on the server (Dashboard, General, Quick Connect); the spike checks it.
- A Jellyfin user to test with (Marco's own account is fine; the password is never stored, only the token).

## Out of scope

Posters, browsing inside a library, playback: next milestones.

## Test checklist (Marco, on the headset)

To be completed in the Test phase.

## Phase log

| Phase | Status | Notes |
|---|---|---|
| Research | | JQ-015. |
| Plan | Active | This file, awaiting approval. |
| Code | | |
| Build | | |
| Test | | |
| Release | | Version 0.3.0. |
| Deploy | | |
| Review | | |
