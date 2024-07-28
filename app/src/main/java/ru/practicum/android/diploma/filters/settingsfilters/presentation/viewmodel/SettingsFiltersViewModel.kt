package ru.practicum.android.diploma.filters.settingsfilters.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.common.domain.FiltersAll
import ru.practicum.android.diploma.filters.choosearea.domain.api.ChooseAreaInteractor
import ru.practicum.android.diploma.filters.choosearea.domain.models.AreaInfo
import ru.practicum.android.diploma.filters.chooseindustry.domain.interfaces.IndustryInteractor
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustriesModel
import ru.practicum.android.diploma.filters.settingsfilters.domain.api.SettingsInteractor
import ru.practicum.android.diploma.filters.settingsfilters.domain.models.SalaryFilters

class SettingsFiltersViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val chooseAreaInteractor: ChooseAreaInteractor,
    private val chooseIndustryInteractor: IndustryInteractor,
) : ViewModel() {

    private var originalFilters: FiltersAll? = null

    private val _filters = MutableLiveData<FiltersAll?>()
    fun observeFilters(): LiveData<FiltersAll?> = _filters

    init {
        originalFilters = getAllFilters()
    }

    private fun getAllFilters(): FiltersAll {
        return FiltersAll(
            salary = getSalaryFilters(),
            area = getAreaSettings(),
            industry = getIndustrySettings()
        )
    }

    fun reloadFilters() {
        _filters.value = getAllFilters()
    }

    fun saveSalaryCheckbox(checked: Boolean) {
        val currentSalaryFilters = settingsInteractor.getSalaryFilters()
        settingsInteractor.saveSalaryFilters(
            SalaryFilters(
                checkbox = checked,
                salary = currentSalaryFilters?.salary
            )
        )
    }

    fun saveSalarySum(amount: String) {
        val currentSalaryFilters = settingsInteractor.getSalaryFilters()
        settingsInteractor.saveSalaryFilters(
            SalaryFilters(
                checkbox = currentSalaryFilters?.checkbox ?: false,
                salary = amount
            )
        )
    }

    fun getAreaSettings(): AreaInfo? {
        return chooseAreaInteractor.getAreaSettings()
    }

    fun getIndustrySettings(): IndustriesModel? {
        return chooseIndustryInteractor.getIndustrySettings()
    }

    fun getSalaryFilters(): SalaryFilters? {
        return settingsInteractor.getSalaryFilters()
    }

    fun clearAreaSettings() {
        chooseAreaInteractor.deleteAreaSettings()
    }

    fun clearIndustrySettings() {
        chooseIndustryInteractor.deleteIndustrySettings()
    }

    fun clearSalary() {
        settingsInteractor.saveSalaryFilters(
            SalaryFilters(
                checkbox = false,
                salary = null
            )
        )
    }

    fun savePreviousAreaSettings(area: AreaInfo) {
        chooseAreaInteractor.savePreviousAreaSettings(area)
    }

    // Сброс всех фильтров
    fun resetFilters() {
        clearSalary()
        clearAreaSettings()
        clearIndustrySettings()
    }

    fun getOriginalFilters(): FiltersAll? {
        return originalFilters
    }

    fun savePreviousFilters() {
        settingsInteractor.savePreviousFilters(originalFilters)
    }

    fun deletePreviousFilters() {
        settingsInteractor.deletePreviousFilters()
    }

    fun hasPreviousFilters(): Boolean {
        val previous = settingsInteractor.getPreviousFilters()
        return previous != null
    }
}
