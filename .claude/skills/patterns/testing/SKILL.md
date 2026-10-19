---
name: patterns/testing
description: Testing patterns — Kotest BehaviorSpec, fakes, dispatchers, Turbine, ViewModel tests. Read before writing any *Test.kt.
---

# Testing

- Kotest `BehaviorSpec`, Given/When/Then. Files in `src/test/kotlin`.
- Always `extension(TestDispatchersExtension())` — swaps Main + use case dispatchers.
- Fakes by hand. No mock library. Fake = tiny real impl (`MutableStateFlow` repo).
- Event bus: `RecordingEventBus()` from `core:testing`, assert `bus.emitted`.

## Use case
```kotlin
class CheckoutUseCaseTest : BehaviorSpec({
    extension(TestDispatchersExtension())
    Given("an empty cart") {
        val repo = FakeCartRepository()
        When("checkout") {
            Then("EmptyCart is thrown") {
                shouldThrow<CartException.EmptyCart> { CheckoutUseCase(repo, RecordingEventBus())().single() }
            }
        }
    }
})
```
- One-shot: `.single()`. Stream: `.first()` or Turbine `test { awaitItem() }`.

## ViewModel
- Build VM with real use cases over fake repos (or fake use case impls of the `api` interface).
- Call `onAction(...)`, assert `viewModel.uiState.value`.
- Error: assert `baseUiState.errorDialogState.message == R.string.x`.

## Rules
- Bug fix ships a test that fails without the fix.
- Name = behavior ("total doubles"), not method name.
- Run: `./gradlew :feature:cart:domain:testDebugUnitTest`.
