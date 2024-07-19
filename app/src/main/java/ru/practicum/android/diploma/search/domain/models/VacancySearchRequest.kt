package ru.practicum.android.diploma.search.domain.models

data class VacancySearchRequest(
    val expression: String,
    val page: Int?,
    val perPage: Int?,
    val filters: Filters,
)

data class Filters(
    val areaId: String?,
    val industryId: String?,
    val salary: Int?,
    val onlyWithSalary: Boolean = false,
)
