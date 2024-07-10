package ru.practicum.android.diploma.favorites.presentation.models

import ru.practicum.android.diploma.common.domain.ErrorType
import ru.practicum.android.diploma.common.domain.VacancyBase
import ru.practicum.android.diploma.search.presentation.models.SearchState
import java.lang.Exception

sealed interface FavouritesStates {
    object Empty : FavouritesStates
    class NotEmpty(val vacancies: ArrayList<VacancyBase>) : FavouritesStates
    data class Error(val errorType: Exception) : FavouritesStates
}
