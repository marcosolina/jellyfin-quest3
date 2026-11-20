# Changelog

All notable changes to the app. Versions follow semantic versioning ([process.md](docs/process.md#source-control-and-versions)).

## 0.1.0 (2026-11-20)

Milestone 1: hello world on the Quest 3 ([iteration 2](docs/iterations/iteration-02-hello-world.md)).

### Added

- Immersive app for the Meta Quest 3, built with Meta Spatial SDK 0.15 and Kotlin, installed from the PC.
- A floating panel over passthrough with a greeting, the version, and a button that hides or shows the room.
- The panel appears in front of the viewer, also after putting the headset on, and can be moved by its handle bar.
- List of the headset's video and audio decoders, also written to the log.
- One command to build, install and start a development build on the headset: `./gradlew runOnQuest`.
