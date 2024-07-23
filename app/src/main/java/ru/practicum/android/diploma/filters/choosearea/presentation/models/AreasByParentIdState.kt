package ru.practicum.android.diploma.filters.choosearea.presentation.models

import ru.practicum.android.diploma.common.domain.ErrorType
import ru.practicum.android.diploma.filters.choosearea.domain.models.AreaInfo

sealed interface AreasByParentIdState {

    data object Loading : AreasByParentIdState
    data object Empty : AreasByParentIdState
    data class Content(val areas: List<AreaInfo>) :
        AreasByParentIdState

    data class Error(val errorType: ErrorType) : AreasByParentIdState
}
