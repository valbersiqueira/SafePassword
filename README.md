# SafePassword

Android app for **secure password management**, built with **Clean Architecture + MVVM + Jetpack Compose + Hilt**.

---

## Features

- Secure authentication via email and password
- Local credential storage (Room)
- Internationalization: supports Portuguese (pt-BR) and English (en)
- Light and dark theme with Material Design 3
- Animated splash screen
- Real-time email and password validation

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Kotlin | Main language |
| Jetpack Compose | Declarative UI |
| Hilt | Dependency injection |
| StateFlow / SharedFlow | State management |
| Navigation Compose | Screen navigation |
| Retrofit + OkHttp | REST API calls |
| Room | Local persistence |
| Coroutines | Asynchronous programming |
| Coil | Image loading |

---

## Project Structure

```
app/src/main/java/com/safepassword/app/
│
├── data/                          # Data layer
│   ├── api/
│   │   ├── ApiService.kt          # Retrofit interface with endpoints
│   │   └── ApiResult.kt           # Sealed class for result + safeApiCall
│   ├── di/
│   │   ├── NetworkModule.kt       # Provides Retrofit, OkHttp, ApiService
│   │   └── RepositoryModule.kt    # Binds interfaces to implementations
│   ├── mappers/
│   │   ├── AuthMapper.kt          # Converts LoginResponse → AuthResult
│   │   └── SampleItemMapper.kt
│   ├── models/
│   │   ├── AuthDto.kt             # LoginRequest, LoginResponse, UserResponse
│   │   └── SampleItemDto.kt
│   └── repositories/
│       ├── AuthRepositoryImpl.kt
│       └── SampleRepositoryImpl.kt
│
├── domain/                        # Domain layer (no external dependencies)
│   ├── models/
│   │   ├── Auth.kt                # User, Credentials, AuthResult
│   │   └── SampleItem.kt
│   ├── repositories/
│   │   ├── AuthRepository.kt      # Auth repository interface
│   │   └── SampleRepository.kt
│   └── usecases/
│       ├── LoginUseCase.kt
│       ├── ValidateEmailUseCase.kt
│       ├── ValidatePasswordUseCase.kt
│       └── GetSampleItemsUseCase.kt
│
├── presentation/                  # Presentation layer
│   └── ui/
│       ├── navigation/
│       │   ├── Screen.kt          # Sealed class with routes
│       │   └── NavGraph.kt        # NavHost Compose

## Setup

### Requirements

- Android Studio Hedgehog or newer
- JDK 17
- Android SDK 34
- minSdk 24

### Configure API URL

In `app/build.gradle.kts`:
```kotlin
buildConfigField("String", "API_BASE_URL", "\"https://your-api.com/\"")
```

### Add a new Feature (pattern)

Always use this structure for each new feature:

```
├── repositories/        # NovaFeatureRepository.kt (interface)
├── components/          # Small, reusable composables
├── screens/             # NewFeatureScreen.kt
└── viewmodels/          # NewFeatureViewModel.kt + UiState + UiEvent

domain/
├── models/              # NewEntity.kt
├── repositories/        # NewFeatureRepository.kt (interface)
└── usecases/            # GetNewFeatureUseCase.kt

data/
├── models/              # NewEntityDto.kt
├── mappers/             # NewEntityMapper.kt
└── repositories/        # NewFeatureRepositoryImpl.kt
```

### Add a new screen to navigation

In `Screen.kt`:
```kotlin
object NewFeature : Screen("new_feature")
```

In `NavGraph.kt`:
```kotlin
composable(Screen.NewFeature.route) {
    NewFeatureScreen(/* callbacks */)
}
```
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

## Mandatory Patterns

### ViewModel
- Use `StateFlow` for UI state (`UiState`)
- Use `SharedFlow` for one-time events (`UiEvent`)
- Use `viewModelScope` for coroutines
- Annotate with `@HiltViewModel`

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
- Public screen gets ViewModel via `hiltViewModel()`
- Internal content is private and only receives data/callbacks
- Always create a `@Preview` for the internal content

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
- One use case = one business action
- Annotate dependencies with `@Inject`
- Return `Flow` for streams, direct value for single operations

### Repository
- Interface in domain, implementation in data layer
- Use `safeApiCall` to wrap Retrofit calls
- Use `flowOn(Dispatchers.IO)` for I/O operations

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
