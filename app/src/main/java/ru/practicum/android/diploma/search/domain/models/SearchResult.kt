package ru.practicum.android.diploma.search.domain.models

class SearchResult(
val page: Int,
val perPage: Int,
val pages: Int,
val found: Int,
val vacancies: List<VacancySearchResultItem>
)
