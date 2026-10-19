# Krakathon — Claude rules

Android app. Kotlin, Compose, Koin, Ktor, Navigation 3, Kotest, Konsist.
Package `com.castorama.krakathon`. Everything English.

## Modules
```
app/                      wiring only: Koin start, NavDisplay
core/usecase              UseCase, BaseUseCase, flowSingle, dispatchers
core/exception            AppException hierarchy, ErrorType
core/events               AppEvent, AppEventBus
core/presentation         BaseViewModel, MVI state, launchWith, error -> string
core/designsystem         KrakTheme + Krak* components
core/navigation           Navigator, FeatureEntries
core/network              Ktor HttpClient, safeApiCall
core/testing              Kotest helpers, fakes
feature/<x>/api           shareable: routes, models, use case interfaces, events, exceptions
feature/<x>/domain        use cases, repository interfaces, private models
feature/<x>/data          repository impl, data sources (fake + remote)
feature/<x>/ui            ViewModel, Screen, Entries, strings
konsist/                  architecture tests
```
Core = infrastructure only. No business. No "common" module, ever — name by concern.

## Dependency rules (build fails if broken)
- `api` -> core only. Never a feature.
- `domain` -> own `api`, other `api`, core.
- `data` -> own `domain` + `api`, other `api`, core.
- `ui` -> own `domain` + `api`, other `api`, core. Never `data`.
- Other feature? ONLY its `api`. Need more? Move it to that `api`.

## Golden rules
1. Inject by constructor. Koin: `factoryOf` use case, `singleOf` repo, `viewModelOf` VM.
2. ViewModel -> UseCase only. Never repository.
3. VM flows: `.launchWith(viewModelScope, onError)`. No `viewModelScope.launch`, `.first()`, `try`, `runCatching`.
4. Use case extends `BaseUseCase` / `NoParamUseCase`. One-shot = `flowSingle { }`. No `Dispatchers`.
5. Errors = typed exceptions (`AppException`). Throw, no `Result`.
6. DataSource wraps calls in `safeApiCall { }`. No try/catch.
7. State other screens see = `StateFlow` on singleton repo. One-off fact = `AppEvent` on `AppEventBus`.
   Read-then-write on shared state = ONE atomic repo call (`takeAll()`), never read + clear.
8. Composables dumb. Stateful `XEntry` + stateless `XScreen(state, onAction)`.
   Screen data = `Loadable<T>` (Loading / Ready / Failed). VM: `useCase().launchLoadable { copy(x = it) }`. Screen: `LoadableContent(...)`. Failed load = inline error + retry, never endless skeleton.
9. UI only `Krak*` components + `KrakTheme` tokens. No raw colors/dp guesses.
10. Strings in `res/values/strings.xml`. No `Text("...")`.
11. `LazyColumn items(...)` always has `key =`.
12. No `// TODO` in code. Open an issue.

## Commands
```
./gradlew assembleDebug                  build app
./gradlew testDebugUnitTest :konsist:test unit + architecture tests
./gradlew checkModules                   module rules + generator tests
./gradlew newFeature --name=wishlist     new feature, 4 modules, wired
```
Done = `assembleDebug` + tests green. Fix code, never the rule.

## Skills / agents
- Layer patterns: `.claude/skills/patterns/{domain,data,ui,testing}`. Read before editing that layer.
- New feature: `/new-feature`. Build: `/build`. Commit: `/commit`. Review: `/review`.
- Agents: `feature-builder` (writes layers), `test-builder` (Kotest), `reviewer` (checks rules).

## Git
Conventional commits: `feat:`, `fix:`, `refactor:`, `test:`, `docs:`. Small PRs, squash merge.
