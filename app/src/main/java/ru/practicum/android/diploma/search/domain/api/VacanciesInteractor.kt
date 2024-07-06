package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.vacancydetail.presentation.models.Vacancy

interface VacanciesInteractor {
    fun findVacancies(expression: String, page: Int?, perPage: Int?): Flow<Pair<List<Vacancy>?, String?>>
}
