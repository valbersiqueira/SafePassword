package com.safepassword.app.domain.repositories

import com.safepassword.app.domain.models.SampleItem
import kotlinx.coroutines.flow.Flow

interface SampleRepository {
    fun getItems(): Flow<List<SampleItem>>
    suspend fun getItemById(id: String): SampleItem?
    suspend fun saveItem(item: SampleItem)
    suspend fun deleteItem(id: String)
}