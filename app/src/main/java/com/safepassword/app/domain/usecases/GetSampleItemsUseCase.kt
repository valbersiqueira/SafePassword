package com.safepassword.app.domain.usecases

import com.safepassword.app.domain.models.SampleItem
import com.safepassword.app.domain.repositories.SampleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSampleItemsUseCase @Inject constructor(
    private val repository: SampleRepository
) {
    operator fun invoke(): Flow<List<SampleItem>> = repository.getItems()
}