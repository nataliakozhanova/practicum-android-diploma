package ru.practicum.android.diploma.search.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.domain.ErrorType
import ru.practicum.android.diploma.common.domain.Resource
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.models.SearchResult
import ru.practicum.android.diploma.search.domain.models.VacancySearchRequest

class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {
    override fun findVacancies(searchRequest: VacancySearchRequest): Flow<Pair<SearchResult?, ErrorType>> {
        return repository.findVacancies(searchRequest)
            .map { result ->
                when (result) {
                    is Resource.Success -> {
                        Pair(result.data, result.error)
                    }

                    is Resource.Error -> {
                        Pair(null, result.error)
                    }
                }
            }
    }
}
