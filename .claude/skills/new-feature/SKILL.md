---
name: new-feature
description: Create a new feature (4 modules, wired into app) and fill it in. Use when adding a screen/area that is not part of an existing feature.
argument-hint: <name>
---

# New feature

1. Name: lowercase, one word (`wishlist`). Not "common", not "shared".
2. `./gradlew newFeature --name=<name>` — makes api/domain/data/ui, adds settings + app deps + Koin modules.
3. `./gradlew assembleDebug` — must be green before you touch anything.
4. Fill layers with `feature-builder` agent (domain -> data -> ui).
5. Link it: some screen calls `navigator.navigate(<Name>Route)`. Feature using it adds `implementation(projects.feature.<name>.api)`.
6. Tests via `test-builder`. Then `/build`.
