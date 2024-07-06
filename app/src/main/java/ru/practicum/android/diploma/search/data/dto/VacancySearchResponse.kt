package ru.practicum.android.diploma.search.data.dto

class VacancySearchResponse(
    val found: Int,
    val items: List<VacancyDto>,
) : Response()
