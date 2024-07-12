package ru.practicum.android.diploma.search.presentation.models

import ru.practicum.android.diploma.common.data.ErrorType
import ru.practicum.android.diploma.common.domain.VacancyBase

sealed interface SearchState {

    data object Loading : SearchState
    data object Empty : SearchState
    data class Content(val vacancies: MutableList<VacancyBase>, val found: Int, val pages: Int, val page: Int) :
        SearchState

    data class Error(val errorType: ErrorType) : SearchState
}
