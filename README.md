# SafePassword

Aplicativo Android para **gerenciamento seguro de senhas**, construído com **Clean Architecture + MVVM + Jetpack Compose + Hilt**.

---

## Funcionalidades

- Autenticação segura por e-mail e senha
- Armazenamento local de credenciais (Room)
- Internacionalização: suporte a Português (pt-BR) e Inglês (en)
- Tema claro e escuro com Material Design 3
- Splash screen animada
- Validação de e-mail e senha em tempo real

---

## Stack Tecnológica

| Tecnologia | Finalidade |
|---|---|
| Kotlin | Linguagem principal |
| Jetpack Compose | UI declarativa |
| Hilt | Injeção de dependência |
| StateFlow / SharedFlow | Gerenciamento de estado |
| Navigation Compose | Navegação entre telas |
| Retrofit + OkHttp | Chamadas de API REST |
| Room | Persistência local |
| Coroutines | Programação assíncrona |
| Coil | Carregamento de imagens |

---

## Estrutura do Projeto

```
app/src/main/java/com/safepassword/app/
│
├── data/                          # Camada de Dados
│   ├── api/
│   │   ├── ApiService.kt          # Interface Retrofit com endpoints
│   │   └── ApiResult.kt           # Sealed class de resultado + safeApiCall
│   ├── di/
│   │   ├── NetworkModule.kt       # Provê Retrofit, OkHttp, ApiService
│   │   └── RepositoryModule.kt    # Bind de interfaces → implementações
│   ├── mappers/
│   │   ├── AuthMapper.kt          # Converte LoginResponse → AuthResult
│   │   └── SampleItemMapper.kt
│   ├── models/
│   │   ├── AuthDto.kt             # LoginRequest, LoginResponse, UserResponse
│   │   └── SampleItemDto.kt
│   └── repositories/
│       ├── AuthRepositoryImpl.kt
│       └── SampleRepositoryImpl.kt
│
├── domain/                        # Camada de Domínio (sem dependências externas)
│   ├── models/
│   │   ├── Auth.kt                # User, Credentials, AuthResult
│   │   └── SampleItem.kt
│   ├── repositories/
│   │   ├── AuthRepository.kt      # Interface de autenticação
│   │   └── SampleRepository.kt
│   └── usecases/
│       ├── LoginUseCase.kt
│       ├── ValidateEmailUseCase.kt
│       ├── ValidatePasswordUseCase.kt
│       └── GetSampleItemsUseCase.kt
│
├── presentation/                  # Camada de Apresentação
│   └── ui/
│       ├── navigation/
│       │   ├── Screen.kt          # Sealed class com rotas
│       │   └── NavGraph.kt        # NavHost Compose
│       ├── splash/
│       │   └── SplashScreen.kt
│       ├── login/
│       │   ├── components/        # EmailField, PasswordField
│       │   ├── screens/           # LoginScreen
│       │   └── viewmodels/        # LoginViewModel, LoginUiState, LoginUiEvent
│       └── home/
│           ├── screens/           # HomeScreen
│           └── viewmodels/        # HomeViewModel, HomeUiState
│
├── ui/theme/                      # Tema global (Material Design 3)
│   ├── Color.kt
│   ├── Theme.kt                   # AppTheme (light/dark)
│   └── Type.kt
│
├── MainActivity.kt                # @AndroidEntryPoint
├── SplashActivity.kt
└── SafePasswordApp.kt             # @HiltAndroidApp
```

---

## Configuração

### Requisitos

- Android Studio Hedgehog ou superior
- JDK 17
- Android SDK 34
- minSdk 24

### Configurar URL da API

Em `app/build.gradle.kts`:
```kotlin
buildConfigField("String", "API_BASE_URL", "\"https://sua-api.com/\"")
```

### Adicionar nova Feature (padrão)

Siga sempre esta estrutura para cada nova feature:

```
presentation/ui/nova_feature/
├── components/          # Composables menores e reutilizáveis
├── screens/             # NovaFeatureScreen.kt
└── viewmodels/          # NovaFeatureViewModel.kt + UiState + UiEvent

domain/
├── models/              # NovaEntidade.kt
├── repositories/        # NovaFeatureRepository.kt (interface)
└── usecases/            # GetNovaFeatureUseCase.kt

data/
├── models/              # NovaEntidadeDto.kt
├── mappers/             # NovaEntidadeMapper.kt
└── repositories/        # NovaFeatureRepositoryImpl.kt
```

### Adicionar nova tela à navegação

Em `Screen.kt`:
```kotlin
object NovaFeature : Screen("nova_feature")
```

Em `NavGraph.kt`:
```kotlin
composable(Screen.NovaFeature.route) {
    NovaFeatureScreen(/* callbacks */)
}
```

---

## Padrões obrigatórios

### ViewModel
- Use `StateFlow` para estado da UI (`UiState`)
- Use `SharedFlow` para eventos únicos (`UiEvent`)
- Use `viewModelScope` para coroutines
- Anote com `@HiltViewModel`

```kotlin
@HiltViewModel
class PasswordViewModel @Inject constructor(
    private val useCase: GetPasswordsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PasswordUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<PasswordUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
}
```

### Composable Screen
- Tela pública recebe ViewModel via `hiltViewModel()`
- Conteúdo interno é privado e recebe apenas dados/callbacks
- Sempre crie `@Preview` para o conteúdo interno

```kotlin
@Composable
fun PasswordScreen(
    onAction: () -> Unit,
    viewModel: PasswordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PasswordContent(uiState = uiState, onAction = onAction)
}

@Composable
private fun PasswordContent(uiState: PasswordUiState, onAction: () -> Unit) { ... }
```

### Use Cases
- Um use case = uma ação de negócio
- Anote dependências com `@Inject`
- Retorne `Flow` para streams, valor direto para operações únicas

### Repositório
- Interface no domínio, implementação na camada de dados
- Use `safeApiCall` para encapsular chamadas Retrofit
- Use `flowOn(Dispatchers.IO)` para operações de I/O

---

## Internationalization (i18n)

The app supports multiple languages via `strings.xml`:

| Resource | Language |
|---|---|
| `res/values/strings.xml` | Portuguese (pt-BR) — default |
| `res/values-en/strings.xml` | English (en) |

To add a new language, create `res/values-XX/strings.xml` where `XX` is the language code (e.g., `es` for Spanish).

---

## Commit Convention

Follow the **Conventional Commits** pattern with messages in **English**:

```
feat(auth): add biometric authentication support
fix(login): fix crash on empty password submission
refactor(vault): extract password strength logic to use case
test(auth): add unit tests for ValidatePasswordUseCase
chore(deps): update Compose BOM to 2024.x
```

Types: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`, `perf`, `ci`, `build`, `revert`

---

## Testing

### Unit (ViewModel)
```kotlin
@Test
fun `when login succeeds should emit NavigateToHome event`() = runTest {
    // Given
    coEvery { loginUseCase(any()) } returns flowOf(fakeAuthResult)

    // When
    viewModel.onLoginClick()

    // Then
    viewModel.uiEvent.test {
        assertEquals(LoginUiEvent.NavigateToHome, awaitItem())
    }
}
```

### Use Case
```kotlin
@Test
fun `validateEmail returns false for invalid email`() {
    val useCase = ValidateEmailUseCase()
    assertFalse(useCase("invalidemail"))
    assertTrue(useCase("valid@email.com"))
}
```

---

## CI/CD (GitHub Actions)

- **Build & Test:** On every push and pull request:
    - Checkout code
    - Set up JDK 17
    - Cache Gradle dependencies
    - Build (assembleDebug)
    - Run unit tests
    - Lint
- **Deploy:** On push to `main`, pre-configured for deploy to Google Play Store via `r0adkll/upload-google-play`.

### Required secrets for deploy

Configure in your GitHub repository:
- `PLAY_STORE_JSON`: Play Store API service account JSON
- `KEYSTORE_BASE64`: Release keystore in Base64

> Deploy will only work when the secrets are configured.

See `.github/workflows/android.yml` for details.
