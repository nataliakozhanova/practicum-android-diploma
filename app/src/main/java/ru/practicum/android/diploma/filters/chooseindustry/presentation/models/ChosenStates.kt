package ru.practicum.android.diploma.filters.chooseindustry.presentation.models

sealed interface ChosenStates {
    data object Chosen : ChosenStates
    data object NotChosen : ChosenStates
}
