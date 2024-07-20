package ru.practicum.android.diploma.filters.settingsfilters.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filters.choosearea.domain.api.ChooseAreaInteractor
import ru.practicum.android.diploma.filters.choosearea.domain.models.AreaInfo
import ru.practicum.android.diploma.filters.chooseindustry.domain.interfaces.IndustryInteractor
import ru.practicum.android.diploma.filters.settingsfilters.domain.api.SettingsInteractor
import ru.practicum.android.diploma.filters.settingsfilters.domain.models.SalaryFilters

class SettingsFiltersViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val chooseAreaInteractor: ChooseAreaInteractor,
    private val chooseIndustryInteractor: IndustryInteractor
) : ViewModel() {

    private var salaryFilters = settingsInteractor.getSalaryFilters()

    private val _state = MutableLiveData<SalaryFilters?>()
    fun observeFilters(): LiveData<SalaryFilters?> = _state

    init {
        _state.value = salaryFilters
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

    /*fun clearIndustrySettings() {
        chooseIndustryInteractor.deleteIndustrySettings()
    }*/

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
        settingsInteractor.saveSalaryFilters(salaryFilters!!)
    }

    fun resetFilters() {
        // Сброс всех фильтров
        clearSalary()
        clearAreaSettings()
        // clearIndustrySettings()
        salaryFilters = SalaryFilters(checkbox = false, salary = null)
        settingsInteractor.saveSalaryFilters(salaryFilters!!)
        _state.value = salaryFilters
    }

    fun getSalaryFilters(): SalaryFilters? {
        return salaryFilters
    }

}
