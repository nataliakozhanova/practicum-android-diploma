package ru.practicum.android.diploma.search.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.common.data.ResponseBase

class VacancySearchResponse(
    val found: Int,
    val page: Int,
    val pages: Int,
    @SerializedName("per_page")
    val perPage: Int,
    val items: List<VacancySearchDto>,
) : ResponseBase()
