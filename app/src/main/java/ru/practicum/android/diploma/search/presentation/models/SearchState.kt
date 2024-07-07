package ru.practicum.android.diploma.search.presentation.models

import ru.practicum.android.diploma.common.domain.ErrorType
import ru.practicum.android.diploma.vacancydetails.presentation.models.Vacancy

sealed interface SearchState {

    data object Loading : SearchState
    data object Empty : SearchState
    data class Content(val vacancies: List<Vacancy>) : SearchState
    data class Error(val errorType: ErrorType) : SearchState
}
