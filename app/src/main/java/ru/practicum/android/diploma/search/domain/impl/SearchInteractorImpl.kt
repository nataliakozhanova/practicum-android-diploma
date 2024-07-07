package ru.practicum.android.diploma.search.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.domain.ErrorType
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.common.domain.Resource
import ru.practicum.android.diploma.vacancydetails.presentation.models.Vacancy

class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {
    override fun findVacancies(expression: String, page: Int?, perPage: Int?): Flow<Pair<List<Vacancy>?, ErrorType>> {
        return repository.findVacancies(expression, page, perPage).map { result ->
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
