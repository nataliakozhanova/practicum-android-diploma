package ru.practicum.android.diploma.search.presentation.models

import ru.practicum.android.diploma.vacancydetail.presentation.models.Vacancy

sealed interface SearchState {

    data object Loading : SearchState
    data class Content(val vacancies: List<Vacancy>) : SearchState
    data class Empty(val message: String) : SearchState
    data class Error(val errorMessage: String) : SearchState
}
