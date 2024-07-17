package ru.practicum.android.diploma.search.data.dto

data class VacancySearchRequest(
    val expression: String,
    val page: Int?,
    val perPage: Int?,
    // val filters: Filters,
)

data class FiltersDto(
    val areaId: Int?,
    val industryId: Int?,
    val salary: Int?,
    val onlyWithSalary: Boolean = false,
)
