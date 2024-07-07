package ru.practicum.android.diploma.search.data.dto

class VacancySearchResponse(
    val found: Int,
    val page: Int,
    val pages: Int,
    val per_page: Int,
    val items: List<VacancyDto>,
) : Response()
