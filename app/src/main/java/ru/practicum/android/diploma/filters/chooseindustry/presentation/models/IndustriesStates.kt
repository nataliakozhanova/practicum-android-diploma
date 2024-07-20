package ru.practicum.android.diploma.filters.chooseindustry.presentation.models

import ru.practicum.android.diploma.common.data.ErrorType
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustriesModel

sealed interface IndustriesStates {
    data object Loading : IndustriesStates
    data object Empty : IndustriesStates
    data class Content(val industries: List<IndustriesModel>) :
        IndustriesStates

    data class Error(val errorType: ErrorType) : IndustriesStates
}
