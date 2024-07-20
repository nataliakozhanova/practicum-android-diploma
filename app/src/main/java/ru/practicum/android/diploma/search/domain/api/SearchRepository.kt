package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.data.Resource
import ru.practicum.android.diploma.search.domain.models.SearchResult
import ru.practicum.android.diploma.search.domain.models.VacancySearchRequest

interface SearchRepository {
    fun findVacancies(searchRequest: VacancySearchRequest): Flow<Resource<SearchResult?>>
}
