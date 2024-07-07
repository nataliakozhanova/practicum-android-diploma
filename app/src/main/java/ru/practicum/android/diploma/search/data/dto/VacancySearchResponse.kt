package ru.practicum.android.diploma.search.data.dto

import com.google.gson.annotations.SerializedName

class VacancySearchResponse(
    val found: Int,
    val page: Int,
    val pages: Int,
    @SerializedName("per_page")
    val perPage: Int,
    val items: List<VacancyDto>,
) : Response()
