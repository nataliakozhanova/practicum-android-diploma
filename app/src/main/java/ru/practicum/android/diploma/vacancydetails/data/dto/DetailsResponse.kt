package ru.practicum.android.diploma.vacancydetails.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.common.data.ResponseBase
import ru.practicum.android.diploma.search.data.dto.VacancyDto

class DetailsResponse(
    val found: Int,
    val page: Int,
    val pages: Int,
    @SerializedName("per_page")
    val perPage: Int,
    val items: List<VacancyDto>,
) : ResponseBase()
