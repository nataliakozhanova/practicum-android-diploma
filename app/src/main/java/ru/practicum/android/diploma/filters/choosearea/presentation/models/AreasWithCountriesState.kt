package ru.practicum.android.diploma.filters.choosearea.presentation.models

import ru.practicum.android.diploma.common.data.ErrorType
import ru.practicum.android.diploma.filters.choosearea.domain.models.AreaInfo

sealed interface AreasWithCountriesState {

    data object Loading : AreasWithCountriesState
    data object Empty : AreasWithCountriesState
    data class Content(val areasWithCountry: List<AreaInfo>) :
        AreasWithCountriesState

    data class Error(val errorType: ErrorType) : AreasWithCountriesState
}
