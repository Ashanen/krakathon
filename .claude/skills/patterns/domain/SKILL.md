---
name: patterns/domain
description: Domain + api layer patterns — use cases, repository interfaces, models, exceptions, events. Read before editing feature/*/domain or feature/*/api.
---

# Domain + api

## Where things go
- `api`: shared with other features. Routes (`@Serializable ... : NavKey`), public models, use case INTERFACES, `AppEvent`s, feature exceptions.
- `domain`: repository interface, use case impls, models nobody else needs.

## Use case
```kotlin
class GetProductsUseCase(
    private val repository: ProductRepository,
) : NoParamUseCase<List<Product>>() {
    override fun onBuild(params: Unit) = flowSingle { repository.getProducts().sortedBy { it.name } }
}
```
- Extend `BaseUseCase<In, Out>` or `NoParamUseCase<Out>`. Runs on background dispatcher by itself.
- One-shot: `flowSingle { }`. Stream: return repo `Flow`.
- `.first()` only inside `flowSingle`.
- Need two use cases together? Make one orchestrator use case. VM never chains.
- No `Dispatchers`. No Android imports.

## Shared use case (other features call it)
```kotlin
// api
interface GetProductUseCase : UseCase<String, Product>
// domain
internal class GetProductUseCaseImpl(repo: ProductRepository) : BaseUseCase<String, Product>(), GetProductUseCase { ... }
// Koin
factoryOf(::GetProductUseCaseImpl) bind GetProductUseCase::class
```

## Errors
Throw typed exceptions. No `Result`.
```kotlin
sealed class CartException : Exception(), AppException { data object EmptyCart : CartException() }
```
Generic ones in `core/exception` (`NetworkException`, `ValidationException`, ...).

## State vs events
- Other screens must SEE it (cart count, favorites)? `StateFlow<Map<Id, Value>>` on singleton repo.
- One-off FACT (order placed)? `data class OrderPlaced(...) : AppEvent` in `api`, `eventBus.emit()` in a use case, `eventBus.observe<OrderPlaced>()` in another use case.
- Never `SharedFlow<Unit>` "refresh" events. Never callbacks between screens.
- Read-modify-write on repo state = one atomic repo method (`MutableStateFlow.getAndUpdate`). Never `value` then `clear()`.
- Stream joins another feature? Catch the EXPECTED miss (`ProductNotFound`) per item and model it (`unavailableIds`); let real errors reach the VM for retry.

## Koin
`factoryOf(::X)` for use cases. Module name `<feature>DomainModule`.
