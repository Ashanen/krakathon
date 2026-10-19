package krakathon

/** Source templates for [NewFeatureTask]. Same shape as the sample features. */
internal class FeatureTemplates(private val name: String) {
    private val type = name.capitalized()
    private val pkg = "$BASE_PACKAGE.feature.$name"
    private fun src(layer: String, file: String) = "$layer/src/main/kotlin/${pkg.replace('.', '/')}/$layer/$file"

    fun files(): Map<String, String> = mapOf(
        "api/build.gradle.kts" to buildFile("api"),
        "domain/build.gradle.kts" to buildFile("domain"),
        "data/build.gradle.kts" to buildFile("data"),
        "ui/build.gradle.kts" to buildFile("ui"),
        src("api", "${type}Route.kt") to route(),
        src("domain", "${type}Repository.kt") to repository(),
        src("domain", "Get${type}MessageUseCase.kt") to useCase(),
        src("domain", "di/${type}DomainModule.kt") to domainModule(),
        src("data", "${type}RepositoryImpl.kt") to repositoryImpl(),
        src("data", "di/${type}DataModule.kt") to dataModule(),
        src("ui", "${type}ViewModel.kt") to viewModel(),
        src("ui", "${type}Screen.kt") to screen(),
        src("ui", "${type}Entries.kt") to entries(),
        src("ui", "di/${type}UiModule.kt") to uiModule(),
        "ui/src/main/res/values/strings.xml" to strings(),
        "domain/src/test/kotlin/${pkg.replace('.', '/')}/domain/Get${type}MessageUseCaseTest.kt" to useCaseTest(),
    )

    private fun buildFile(layer: String) = """
        |plugins {
        |    id("krakathon.feature.$layer")
        |}
        |""".trimMargin()

    private fun route() = """
        |package $pkg.api
        |
        |import androidx.navigation3.runtime.NavKey
        |import kotlinx.serialization.Serializable
        |
        |@Serializable
        |data object ${type}Route : NavKey
        |""".trimMargin()

    private fun repository() = """
        |package $pkg.domain
        |
        |interface ${type}Repository {
        |    suspend fun getMessage(): String
        |}
        |""".trimMargin()

    private fun useCase() = """
        |package $pkg.domain
        |
        |import com.castorama.krakathon.core.usecase.NoParamUseCase
        |import com.castorama.krakathon.core.usecase.flowSingle
        |import kotlinx.coroutines.flow.Flow
        |
        |class Get${type}MessageUseCase(
        |    private val repository: ${type}Repository,
        |) : NoParamUseCase<String>() {
        |    override fun onBuild(params: Unit): Flow<String> = flowSingle { repository.getMessage() }
        |}
        |""".trimMargin()

    private fun domainModule() = """
        |package $pkg.domain.di
        |
        |import $pkg.domain.Get${type}MessageUseCase
        |import org.koin.core.module.dsl.factoryOf
        |import org.koin.dsl.module
        |
        |val ${name}DomainModule = module {
        |    factoryOf(::Get${type}MessageUseCase)
        |}
        |""".trimMargin()

    private fun repositoryImpl() = """
        |package $pkg.data
        |
        |import $pkg.domain.${type}Repository
        |
        |internal class ${type}RepositoryImpl : ${type}Repository {
        |    override suspend fun getMessage(): String = "$type works"
        |}
        |""".trimMargin()

    private fun dataModule() = """
        |package $pkg.data.di
        |
        |import $pkg.data.${type}RepositoryImpl
        |import $pkg.domain.${type}Repository
        |import org.koin.core.module.dsl.singleOf
        |import org.koin.dsl.bind
        |import org.koin.dsl.module
        |
        |val ${name}DataModule = module {
        |    singleOf(::${type}RepositoryImpl) bind ${type}Repository::class
        |}
        |""".trimMargin()

    private fun viewModel() = """
        |package $pkg.ui
        |
        |import androidx.lifecycle.viewModelScope
        |import com.castorama.krakathon.core.presentation.flow.launchWith
        |import com.castorama.krakathon.core.presentation.mvi.BaseUiState
        |import com.castorama.krakathon.core.presentation.mvi.ScreenState
        |import com.castorama.krakathon.core.presentation.viewmodel.BaseViewModel
        |import com.castorama.krakathon.core.usecase.invoke
        |import $pkg.domain.Get${type}MessageUseCase
        |import kotlinx.coroutines.flow.onEach
        |import kotlinx.coroutines.flow.onStart
        |
        |data class ${type}State(
        |    val message: String = "",
        |    override val baseUiState: BaseUiState = BaseUiState(),
        |) : ScreenState<${type}State> {
        |    override fun withBaseUiState(baseUiState: BaseUiState) = copy(baseUiState = baseUiState)
        |}
        |
        |sealed interface ${type}Action {
        |    data object Refresh : ${type}Action
        |}
        |
        |class ${type}ViewModel(
        |    private val get${type}MessageUseCase: Get${type}MessageUseCase,
        |) : BaseViewModel<${type}State, ${type}Action>(${type}State()) {
        |
        |    init {
        |        load()
        |    }
        |
        |    override fun onAction(action: ${type}Action) {
        |        when (action) {
        |            ${type}Action.Refresh -> load()
        |        }
        |    }
        |
        |    private fun load() {
        |        get${type}MessageUseCase()
        |            .onStart { setLoading(true) }
        |            .onEach { message ->
        |                setLoading(false)
        |                updateState { copy(message = message) }
        |            }
        |            .launchWith(viewModelScope, onError = { showError(it, retry = ::load) })
        |    }
        |}
        |""".trimMargin()

    private fun screen() = """
        |package $pkg.ui
        |
        |import androidx.compose.foundation.layout.Box
        |import androidx.compose.foundation.layout.fillMaxSize
        |import androidx.compose.foundation.layout.padding
        |import androidx.compose.material3.Text
        |import androidx.compose.runtime.Composable
        |import androidx.compose.runtime.getValue
        |import androidx.compose.ui.Alignment
        |import androidx.compose.ui.Modifier
        |import androidx.compose.ui.res.stringResource
        |import androidx.lifecycle.compose.collectAsStateWithLifecycle
        |import com.castorama.krakathon.core.designsystem.components.KrakScreen
        |import com.castorama.krakathon.core.presentation.mvi.BaseAction
        |import org.koin.compose.viewmodel.koinViewModel
        |
        |@Composable
        |fun ${type}Entry(onBack: () -> Unit, viewModel: ${type}ViewModel = koinViewModel()) {
        |    val state by viewModel.uiState.collectAsStateWithLifecycle()
        |    ${type}Screen(state = state, onBaseAction = viewModel::onBaseAction, onBack = onBack)
        |}
        |
        |@Composable
        |fun ${type}Screen(state: ${type}State, onBaseAction: (BaseAction) -> Unit, onBack: () -> Unit) {
        |    KrakScreen(
        |        title = stringResource(R.string.${name}_title),
        |        baseUiState = state.baseUiState,
        |        onBaseAction = onBaseAction,
        |        onBack = onBack,
        |    ) { padding ->
        |        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
        |            Text(state.message)
        |        }
        |    }
        |}
        |""".trimMargin()

    private fun entries() = """
        |package $pkg.ui
        |
        |import androidx.navigation3.runtime.EntryProviderScope
        |import androidx.navigation3.runtime.NavKey
        |import com.castorama.krakathon.core.navigation.FeatureEntries
        |import com.castorama.krakathon.core.navigation.Navigator
        |import $pkg.api.${type}Route
        |
        |internal class ${type}Entries : FeatureEntries {
        |    override fun EntryProviderScope<NavKey>.register(navigator: Navigator) {
        |        entry<${type}Route> { ${type}Entry(onBack = navigator::back) }
        |    }
        |}
        |""".trimMargin()

    private fun uiModule() = """
        |package $pkg.ui.di
        |
        |import com.castorama.krakathon.core.navigation.FeatureEntries
        |import $pkg.ui.${type}Entries
        |import $pkg.ui.${type}ViewModel
        |import org.koin.core.module.dsl.singleOf
        |import org.koin.core.module.dsl.viewModelOf
        |import org.koin.dsl.bind
        |import org.koin.dsl.module
        |
        |val ${name}UiModule = module {
        |    viewModelOf(::${type}ViewModel)
        |    singleOf(::${type}Entries) bind FeatureEntries::class
        |}
        |""".trimMargin()

    private fun strings() = """
        |<resources>
        |    <string name="${name}_title">$type</string>
        |</resources>
        |""".trimMargin()

    private fun useCaseTest() = """
        |package $pkg.domain
        |
        |import com.castorama.krakathon.core.testing.TestDispatchersExtension
        |import com.castorama.krakathon.core.usecase.invoke
        |import io.kotest.core.spec.style.BehaviorSpec
        |import io.kotest.matchers.shouldBe
        |import kotlinx.coroutines.flow.first
        |
        |class Get${type}MessageUseCaseTest : BehaviorSpec({
        |    extension(TestDispatchersExtension())
        |
        |    Given("a repository with a message") {
        |        val useCase = Get${type}MessageUseCase(Fake${type}Repository("hello"))
        |
        |        When("the use case runs") {
        |            val result = useCase().first()
        |
        |            Then("it emits the message") {
        |                result shouldBe "hello"
        |            }
        |        }
        |    }
        |})
        |
        |private class Fake${type}Repository(private val message: String) : ${type}Repository {
        |    override suspend fun getMessage() = message
        |}
        |""".trimMargin()
}
