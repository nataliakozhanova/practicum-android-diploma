package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.vacancy_details.presentation.models.Vacancy

interface VacanciesInteractor {
    fun findVacancies(expression: String): Flow<Pair<List<Vacancy>?, String?>>
}
