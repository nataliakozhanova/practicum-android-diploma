package ru.practicum.android.diploma.filters.choosearea.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filters.choosearea.domain.api.ChooseAreaInteractor
import ru.practicum.android.diploma.filters.choosearea.domain.models.AreaInfo

class ChooseAreaViewModel(
    private val chooseAreaInteractor: ChooseAreaInteractor,
) : ViewModel() {

    private val _areaSettings = MutableLiveData<AreaInfo?>()
    val areaSettings: LiveData<AreaInfo?> = _areaSettings

    init {
        loadAreaSettings()
    }

    private fun loadAreaSettings() {
        _areaSettings.value = chooseAreaInteractor.getAreaSettings()
    }

    fun getAreaSettings(): AreaInfo? {
        return _areaSettings.value ?: chooseAreaInteractor.getAreaSettings()
    }

    fun deleteCountrySettings() {
        chooseAreaInteractor.deleteAreaSettings()
        _areaSettings.value = null
    }

    fun saveAreaSettings(area: AreaInfo) {
        chooseAreaInteractor.saveAreaSettings(area)
        _areaSettings.value = area
    }
}
