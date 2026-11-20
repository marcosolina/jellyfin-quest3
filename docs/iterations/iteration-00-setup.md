# Iteration 0: project setup

**Status:** Closed · **Started:** 2026-11-20 · **Closed:** 2026-11-20

## Goal

Agree on how the work is done and create the structure that tracks it: root `CLAUDE.md` and `README.md`, and the `docs/` folder with process, board, backlog, glossary, research and decisions.

## Scope

| ID | Title | State |
|---|---|---|
| JQ-006 | Create the project structure | Closed |
| JQ-007 | Agree on the way of working | Closed |

## Phase log

| Phase | Status | Notes |
|---|---|---|
| Research | Done | Looked at the Jellyfin GitHub organization: 81 active repositories, no official VR or Quest client. |
| Plan | Done | Marco proposed the iteration cycle and a docs folder kept like an Azure DevOps or Jira board. |
| Code | Done | Documentation only, no application code. |
| Build | Skipped | Nothing to build. |
| Test | Done | Marco approved the structure and [process.md](../process.md). |
| Release | Done | Structure committed and pushed to `main`. |
| Deploy | Skipped | Nothing to install. |
| Review | Done | See below. |

## Acceptance checklist

- [x] Marco agrees with the iteration cycle and the roles.
- [x] Marco agrees with the work item types, states and IDs.
- [x] Marco agrees with the folder structure and file names.
- [x] Marco has reviewed the draft epics in the backlog.

## Decisions from the review

- **Target experience:** a virtual cinema, with 3D and 180/360 video shown in the right mode for the format of each file. Immersive viewing (JQ-005) is core scope, not a later extra.
- **Small milestones:** the walking skeleton is built in steps, each one tested on the headset before the next is defined. The first milestone is a hello world app installed on the Quest, to learn and prove the build and install process.
- **Server access:** Marco gave access to his Jellyfin server for research and testing. The repository is public, so addresses and credentials stay out of it.

## Retrospective

- Went well: agreement on process and structure in one round.
- To keep in mind: the less code written between two tests on the headset, the easier problems are to fix. Milestones stay small.
