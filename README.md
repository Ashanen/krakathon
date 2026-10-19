# Krakathon

Android blueprint for the Castorama hackathon. Clone, build, add your feature.

## Need
- JDK 17+ (21 fine)
- Android Studio (latest stable), Android SDK 37

## Run
```
git clone https://github.com/Ashanen/krakathon.git
cd krakathon
./gradlew installDebug        # or Run 'app' in Android Studio
```
Sample app: product catalog -> product detail -> cart -> checkout. Fake data, no backend needed.

## Add a feature
```
./gradlew newFeature --name=wishlist
```
Makes `feature/wishlist/{api,domain,data,ui}`, wires settings, app deps, Koin. Builds as-is.

## Check
```
./gradlew assembleDebug checkModules testDebugUnitTest :konsist:test
```
- `checkModules` — module dependency rules (build-logic).
- `:konsist:test` — code rules (Konsist + Kotest).

## Rules (short)
- Feature = `api / domain / data / ui`. Other features see only `api`.
- Core = infrastructure only (usecase, exception, events, presentation, designsystem, navigation, network, testing).
- ViewModel -> UseCase -> Repository -> DataSource. Constructor injection (Koin).
- VM flows via `launchWith`. Use cases via `BaseUseCase` + `flowSingle`.
- UI = `Krak*` design system + string resources.

More: [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md). Working with Claude Code: [CLAUDE.md](CLAUDE.md).

## Real backend
Set `API_BASE_URL` in `app/build.gradle.kts`, write `RemoteXDataSource` (see `RemoteProductDataSource`), swap the binding in the feature's data Koin module.

## Stack
Kotlin 2.3, AGP 9.2, Gradle 9.6, Compose (Material 3), Navigation 3, Koin 4, Ktor 3, kotlinx.serialization, Kotest 6, Turbine, Konsist.
