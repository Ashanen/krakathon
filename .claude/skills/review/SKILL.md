---
name: review
description: Checklist to review a Krakathon diff — architecture, ViewModel, data, UI, tests.
---

# Review checklist

Architecture
- [ ] Feature->feature only via `api`. Nothing in `api` that is not needed outside.
- [ ] No new core module named common/utils/shared/misc. Core = infra only.
- [ ] Constructor injection everywhere. No `X()` of a dependency.

Domain / data
- [ ] Use cases extend `BaseUseCase`/`NoParamUseCase`, one-shots use `flowSingle`.
- [ ] Typed exceptions, no `Result`, no swallowed errors.
- [ ] Network in `safeApiCall`. DTOs stay in data. Mappers both ways.
- [ ] Shared state = `StateFlow` on singleton repo; one-off facts = `AppEvent`.

UI
- [ ] VM: use cases only, `launchWith` only, errors via `showError`.
- [ ] Entry/Screen split. Screen stateless.
- [ ] Skeleton while loading, empty state with CTA, no dead end.
- [ ] `Krak*` components + theme tokens. Strings in res. Lazy items keyed.

Tests
- [ ] New logic has Given/When/Then tests. Bug fix has regression test.
- [ ] `./gradlew checkModules testDebugUnitTest :konsist:test` green.
