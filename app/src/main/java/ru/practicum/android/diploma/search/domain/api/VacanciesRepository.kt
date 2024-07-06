package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancydetails.presentation.models.Vacancy

interface VacanciesRepository {
    fun findVacancies(expression: String, page: Int?, perPage: Int?): Flow<Resource<List<Vacancy>>>
}
