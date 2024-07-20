package ru.practicum.android.diploma.search.data.dto

data class VacancySearchRequestDto(
    val expression: String,
    val page: Int?,
    val perPage: Int?,
    // val filters: Filters,
)

data class FiltersDto(
    val areaId: String?,
    val industryId: String?,
    val salary: Int?,
    val onlyWithSalary: Boolean = false,
)
