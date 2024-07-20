package ru.practicum.android.diploma.filters.choosearea.presentation.viewmodel

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filters.choosearea.domain.api.ChooseAreaInteractor
import ru.practicum.android.diploma.filters.choosearea.domain.models.AreaInfo

class ChooseAreaViewModel(
    private val chooseAreaInteractor: ChooseAreaInteractor,
) : ViewModel() {

    fun getAreaSettings(): AreaInfo? {
        return chooseAreaInteractor.getAreaSettings()
    }

    fun deleteCountrySettings() {
        chooseAreaInteractor.deleteAreaSettings()
    }

    fun saveAreaSettings(area: AreaInfo) {
        chooseAreaInteractor.saveAreaSettings(area)
    }
}
