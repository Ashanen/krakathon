# Architecture

## Layers
```
UI          ViewModel ── Use Case ── Repository ── DataSource ── Ktor / fake
(ui)        (ui)         (domain)    (domain if,   (data)
                                      data impl)
```

## Feature = 4 modules
| Module | Holds | May depend on |
|---|---|---|
| `api` | routes, public models, use case interfaces, events, exceptions | core |
| `domain` | repository interfaces, use cases | own api, other api, core |
| `data` | repository impls, data sources, DTOs | own domain + api, other api, core |
| `ui` | ViewModels, screens, nav entries, strings | own domain + api, other api, core |

Feature -> feature only through `api`. Enforced by `checkModules` (runs on every build) and Konsist (imports).

Example in the sample:
- `cart:domain` joins cart items with products via `catalog:api` `GetProductUseCase`.
- `catalog:ui` adds to cart via `cart:api` `AddToCartUseCase`, shows count via `ObserveCartCountUseCase`.
- `cart:domain` publishes `OrderPlaced` (in `cart:api`); `catalog` observes it and shows a snackbar.

No cycles: `api` modules never depend on features.

## Core
| Module | What |
|---|---|
| `core:usecase` | `UseCase`, `BaseUseCase`, `NoParamUseCase`, `flowSingle`, `UseCaseDispatchers` |
| `core:exception` | `AppException` sealed hierarchy, `ErrorType` |
| `core:events` | `AppEvent`, `AppEventBus` |
| `core:presentation` | `BaseViewModel`, `ScreenState`, `BaseUiState`, `launchWith`, error -> string, price format |
| `core:designsystem` | `KrakTheme`, tokens, `Krak*` components, `KrakScreen` |
| `core:navigation` | `Navigator`, `FeatureEntries` |
| `core:network` | `HttpClientFactory`, `safeApiCall` |
| `core:testing` | `TestDispatchersExtension`, `RecordingEventBus` |

## Shared state vs events
- State many screens show: `StateFlow` on a singleton repository (cart quantities).
- One-off fact: `AppEvent` on `AppEventBus` (order placed).

## Navigation
Navigation 3. Each feature `ui` registers routes in a `FeatureEntries` bound in Koin. `app` collects all of them into one `NavDisplay`. Routes live in `api` so other features can navigate to them.

## Errors
Data maps transport errors to `NetworkException` (`safeApiCall`). Domain throws typed exceptions. ViewModel `showError(e, retry)` -> `BaseUiState` -> error dialog in `KrakScreen`. Feature text via `errorMessage(e)` override.
