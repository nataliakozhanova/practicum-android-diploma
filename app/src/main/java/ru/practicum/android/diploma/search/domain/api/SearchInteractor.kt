package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.ErrorType
import ru.practicum.android.diploma.vacancydetails.presentation.models.Vacancy

interface SearchInteractor {
    fun findVacancies(expression: String, page: Int?, perPage: Int?): Flow<Pair<List<Vacancy>?, ErrorType>>
}
