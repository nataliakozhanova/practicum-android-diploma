package ru.practicum.android.diploma.filters.choosearea.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.common.data.ResponseBase

data class AreasCatalogDto(
    val id: String,
    val name: String,
    @SerializedName("parent_id")
    val parentId: String?,
    val areas: List<AreasCatalogDto>,
) : ResponseBase()
