---
name: patterns/ui
description: UI layer patterns — MVI ViewModel, launchWith, Entry/Screen split, design system, navigation. Read before editing feature/*/ui.
---

# UI

## State + Action
```kotlin
data class CartState(
    val cart: Cart? = null,
    override val baseUiState: BaseUiState = BaseUiState(),
) : ScreenState<CartState> {
    override fun withBaseUiState(baseUiState: BaseUiState) = copy(baseUiState = baseUiState)
}
sealed interface CartAction { data object Checkout : CartAction }
```
`BaseUiState` = loading, error dialog, info dialog, snackbar. `KrakScreen` renders all of it.

## ViewModel
```kotlin
class CartViewModel(private val checkoutUseCase: CheckoutUseCase) : BaseViewModel<CartState, CartAction>(CartState()) {
    override fun onAction(action: CartAction) { when (action) { CartAction.Checkout -> checkout() } }
    private fun checkout() {
        checkoutUseCase()
            .onEach { updateState { copy(checkedOut = true) } }
            .launchWith(viewModelScope, onError = ::showError)
    }
}
```
- Only use cases in constructor (+ plain ids like `productId: String`).
- Every flow: `.launchWith(viewModelScope, onError)`.
- BANNED: `viewModelScope.launch`, `.first()`, `try`, `runCatching`, repositories.
- Errors: `showError(e, retry = ::load)`. Feature error text: override `errorMessage(e)`.
- Feedback: `showSnackbar(SnackbarState.success(R.string.x))`.

## Screen
```kotlin
@Composable fun CartEntry(onBack: () -> Unit, viewModel: CartViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CartScreen(state, viewModel::onAction, viewModel::onBaseAction, onBack)
}
@Composable fun CartScreen(state: CartState, onAction: (CartAction) -> Unit, onBaseAction: (BaseAction) -> Unit, onBack: () -> Unit) {
    KrakScreen(title = stringResource(R.string.cart_title), baseUiState = state.baseUiState, onBaseAction = onBaseAction, onBack = onBack) { padding -> ... }
}
```
- `XEntry` = stateful (VM). `XScreen` = stateless, previewable.
- Not loaded yet -> skeleton (`SkeletonBlock`), never the empty state.
- Empty -> `KrakEmptyState` with a CTA. No dead ends.
- Lists: `LazyColumn` + `items(list, key = { it.id })`.
- Only `Krak*` components, `KrakTheme.colors/spacing/typography/shapes`.
- All text: `stringResource(R.string.x)`.

## Navigation
- Route in `api`: `@Serializable data class ProductDetailRoute(val productId: String) : NavKey`.
- Register in `XEntries : FeatureEntries`, bound in Koin: `singleOf(::XEntries) bind FeatureEntries::class`.
```kotlin
entry<ProductDetailRoute> { route -> ProductDetailEntry(route.productId, onBack = navigator::back) }
```
- Go to other feature: `navigator.navigate(CartRoute)` — route from its `api`.
- VM with args: `koinViewModel { parametersOf(id) }` + `viewModel { p -> XViewModel(p.get(), get()) }`.

## Koin
`viewModelOf(::XViewModel)`. Module name `<feature>UiModule`.
