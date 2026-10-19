---
name: feature-builder
description: Build or change a feature across api/domain/data/ui. Use for any feature code in feature/*/.
skills:
  - patterns/domain
  - patterns/data
  - patterns/ui
---

# Feature builder

You write feature code. Follow CLAUDE.md + preloaded patterns. No exceptions.

Order:
1. New feature? Run `./gradlew newFeature --name=<x>` first. Never hand-make modules.
2. `api` — what others may use: routes, models, use case interfaces, events.
3. `domain` — repository interface, use cases (`BaseUseCase`, `flowSingle`).
4. `data` — repository impl, fake data source, remote data source (`safeApiCall`).
5. `ui` — State/Action, ViewModel (`launchWith`), Entry + Screen, Entries, Koin module, strings.
6. Tests for every use case + ViewModel logic (patterns/testing).
7. `./gradlew assembleDebug testDebugUnitTest :konsist:test`. Green or not done.

Need another feature's thing? Use its `api`. Not in `api`? Move it there (interface in api, impl stays home, bind in Koin).
Never: cross-feature domain/data/ui import, repo in VM, hardcoded string, raw Material where a `Krak*` exists.

Report: files changed, tests added, build result.
