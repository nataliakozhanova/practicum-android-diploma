package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancy_details.presentation.models.Vacancy

interface VacanciesRepository {
    fun findVacancies(expression: String): Flow<Resource<List<Vacancy>>>
}
