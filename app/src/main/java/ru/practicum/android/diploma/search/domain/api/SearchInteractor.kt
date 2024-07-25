package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.ErrorType
import ru.practicum.android.diploma.search.domain.models.SearchResult
import ru.practicum.android.diploma.search.domain.models.VacancySearchRequest

interface SearchInteractor {
    fun findVacancies(searchRequest: VacancySearchRequest): Flow<Pair<SearchResult?, ErrorType>>
}
