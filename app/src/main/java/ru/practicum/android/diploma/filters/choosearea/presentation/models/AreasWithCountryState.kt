package ru.practicum.android.diploma.filters.choosearea.presentation.models

import ru.practicum.android.diploma.common.data.ErrorType
import ru.practicum.android.diploma.filters.choosearea.domain.models.AreaInfo

sealed interface AreasWithCountryState {

    data object Loading : AreasWithCountryState
    data object Empty : AreasWithCountryState
    data class Content(val areasWithCountry: List<AreaInfo>) :
        AreasWithCountryState

    data class Error(val errorType: ErrorType) : AreasWithCountryState
}
