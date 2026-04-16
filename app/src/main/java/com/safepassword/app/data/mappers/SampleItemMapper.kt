package com.safepassword.app.data.mappers

import com.safepassword.app.data.models.SampleItemResponse
import com.safepassword.app.domain.models.SampleItem
import javax.inject.Inject

class SampleItemMapper @Inject constructor() {

    fun toDomain(response: SampleItemResponse): SampleItem = SampleItem(
        id = response.id,
        title = response.title,
        description = response.description
    )

    fun toDomainList(responses: List<SampleItemResponse>): List<SampleItem> =
        responses.map { toDomain(it) }
}