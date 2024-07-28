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
    private var salaryFilters = settingsInteractor.getSalaryFilters()

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

    fun setOnlyWithSalary(checked: Boolean) {
        salaryFilters = SalaryFilters(
            checkbox = checked,
            salary = salaryFilters?.salary
        )
        settingsInteractor.saveSalaryFilters(salaryFilters!!)
    }

    fun getAreaSettings(): AreaInfo? {
        return chooseAreaInteractor.getAreaSettings()
    }

    fun clearAreaSettings() {
        chooseAreaInteractor.deleteAreaSettings()
    }

    fun savePreviousAreaSettings(area: AreaInfo) {
        chooseAreaInteractor.savePreviousAreaSettings(area)
    }

    fun clearIndustrySettings() {
        chooseIndustryInteractor.deleteIndustrySettings()
    }

    fun saveSalary(amount: String) {
        salaryFilters = SalaryFilters(
            checkbox = salaryFilters?.checkbox ?: false,
            salary = amount
        )
        settingsInteractor.saveSalaryFilters(salaryFilters!!)
    }

    fun clearSalary() {
        salaryFilters = SalaryFilters(
            checkbox = salaryFilters?.checkbox ?: false,
            salary = null
        )
        settingsInteractor.saveSalaryFilters(salaryFilters!!)
    }

    fun applyFilters() {
        // Сохранение всех текущих настроек фильтра
        if (salaryFilters != null) {
            settingsInteractor.saveSalaryFilters(salaryFilters!!)
        }
    }

    fun resetFilters() {
        // Сброс всех фильтров
        clearSalary()
        clearAreaSettings()
        clearIndustrySettings()
        salaryFilters = SalaryFilters(checkbox = false, salary = null)
        settingsInteractor.saveSalaryFilters(salaryFilters!!)
        // _filters.value = salaryFilters
    }

    fun getSalaryFilters(): SalaryFilters? {
        return settingsInteractor.getSalaryFilters()
    }

    fun getIndustrySettings(): IndustriesModel? {
        return chooseIndustryInteractor.getIndustrySettings()
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
