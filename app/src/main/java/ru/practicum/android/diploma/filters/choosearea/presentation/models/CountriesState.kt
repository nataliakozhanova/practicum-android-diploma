package ru.practicum.android.diploma.filters.choosearea.presentation.models

import ru.practicum.android.diploma.common.data.ErrorType
import ru.practicum.android.diploma.filters.choosearea.domain.models.CountryInfo

sealed interface CountriesState {

    data object Loading : CountriesState
    data object Empty : CountriesState
    data class Content(val areas: List<CountryInfo>) :
        CountriesState

    data class Error(val errorType: ErrorType) : CountriesState
}
