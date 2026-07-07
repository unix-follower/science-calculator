# Copilot Instructions

## Project Overview
- This repository is a Java science calculator library/application organized by domain under `org.example.sciencecalc`.
- Main code lives in `src/main/java/org/example/sciencecalc/**`.
- Tests live in `src/test/java/org/example/sciencecalc/**` and generally mirror the production package structure.

## Build And Validation
- Use the Gradle wrapper for all build and test commands.
- Primary validation command: `./gradlew clean build`
- Run targeted tests while iterating when possible, for example: `./gradlew test --tests org.example.sciencecalc.math.ArithmeticTest`
- The project uses Java toolchain version 25. Keep code compatible with that configuration.

## Code Style
- Follow the existing Java style in the repository instead of reformatting files broadly.
- Keep changes minimal and focused on the requested behavior.
- Prefer clear static utility methods and small, direct implementations over adding unnecessary abstractions.
- Preserve package structure by placing new functionality in the matching domain package such as `math`, `physics`, `finance`, or `health`.
- Use descriptive names; avoid one-letter variables except in conventional short loop scopes.
- Prefer `final` for local variables when that matches surrounding code.

## Testing Expectations
- Add or update JUnit 5 tests for behavior changes.
- Place new tests in the corresponding mirrored package under `src/test/java`.
- Cover normal cases, boundary cases, and invalid input paths when methods perform validation.
- Avoid changing unrelated tests to make a feature pass.

## Quality Gates
- Keep the code compatible with Checkstyle, PMD, and JaCoCo coverage enforcement configured in Gradle.
- Avoid introducing dead code, unused imports, or overly complex control flow.
- Prefer root-cause fixes over surface-level patches.

## Working Conventions
- Do not rename public APIs or move packages unless the task explicitly requires it.
- Do not add new dependencies unless they are necessary and justified.
- When modifying numerical logic, preserve existing semantics unless the requested change is specifically behavioral.
- When adding calculators or utilities, follow the existing static utility pattern used across the codebase.
