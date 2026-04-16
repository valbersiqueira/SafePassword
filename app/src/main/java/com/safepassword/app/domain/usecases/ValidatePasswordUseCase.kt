package com.safepassword.app.domain.usecases

import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {
    operator fun invoke(password: String): Boolean = password.length >= 8
}