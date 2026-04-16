package com.safepassword.app.data.repositories

import com.safepassword.app.data.api.ApiService
import com.safepassword.app.data.api.safeApiCall
import com.safepassword.app.data.api.ApiResult
import com.safepassword.app.data.mappers.SampleItemMapper
import com.safepassword.app.domain.models.SampleItem
import com.safepassword.app.domain.repositories.SampleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SampleRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: SampleItemMapper
) : SampleRepository {

    override fun getItems(): Flow<List<SampleItem>> = flow {
        val result = safeApiCall { apiService.getItems() }
        when (result) {
            is ApiResult.Success -> emit(mapper.toDomainList(result.data))
            is ApiResult.Error -> throw Exception("Erro ${result.code}: ${result.message}")
            is ApiResult.Exception -> throw result.e
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getItemById(id: String): SampleItem? {
        val result = safeApiCall { apiService.getItemById(id) }
        return when (result) {
            is ApiResult.Success -> mapper.toDomain(result.data)
            else -> null
        }
    }

    override suspend fun saveItem(item: SampleItem) {
        // Implementar persistência local ou chamada API conforme necessidade
    }

    override suspend fun deleteItem(id: String) {
        safeApiCall { apiService.deleteItem(id) }
    }
}