package ru.practicum.android.diploma.search.domain.models

data class VacancySearchRequest(
    val expression: String,
    val page: Int?,
    val perPage: Int?,
    val filters: Filters,
)

data class Filters(
    val countryId: String? = "5", // Россия
    val areaId: String? = "1913", // Тульская область
    val industryId: String? = null,
    val salary: Int? = 80000,
    val onlyWithSalary: Boolean = true,
)
