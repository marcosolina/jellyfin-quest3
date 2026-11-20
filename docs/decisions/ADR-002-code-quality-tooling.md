# ADR-002: Code quality tooling

**Status:** Accepted (2026-11-20)

## Context

The process requires that formatting and static checks run as part of the build and that a build with violations is not done ([process.md](../process.md#code-quality)). The tools must work with the stack of [ADR-001](ADR-001-technology-stack.md) at the versions in use: Kotlin 2.4, Android Gradle Plugin 9.5, Gradle 9.9, Java 25.

## Options

| Tool | What it checks | State in November 2026 |
|---|---|---|
| ktlint | Formatting and Kotlin style | 1.8.0, stable; usable through Spotless or the ktlint Gradle plugin |
| Spotless | Runs formatters (ktlint) for all files, with a check and an apply task | 8.11.0, actively maintained |
| ktlint Gradle plugin | Runs ktlint only | 14.2.0 (March 2026) |
| detekt | Code smells: complexity, long methods, large classes | 1.23.8 predates Kotlin 2.4 and AGP 9; 2.0 is still alpha |
| Android Lint | Android specific problems, resources, manifest, API use | Built into the Android build |
| Kotlin compiler | Warnings (deprecations, unused code) | Built in; can treat warnings as errors |

## Decision

- **Spotless with ktlint 1.8.0**, `ktlint_official` style, 120 characters per line, on all Kotlin and Gradle Kotlin files.
- **Android Lint with warnings as errors.** Each exception sits in `app/lint.xml` with its reason.
- **Kotlin warnings as errors.**
- **Unit tests** in the same build.
- **detekt is postponed** until 2.0 is stable (backlog task JQ-024). Until then, complexity and size are watched in the end-of-iteration code review.

`./gradlew build` runs all of them.

## Consequences

- Formatting is never discussed: `./gradlew spotlessApply` fixes it.
- A new version of a library that deprecates an API we use breaks the build until the code is updated. This is intended, but upgrades must be planned.
- Without detekt, nothing measures complexity automatically for now.

## References

- [process.md, Code quality](../process.md#code-quality)
- [architecture.md, Build and checks](../architecture.md#build-and-checks)
