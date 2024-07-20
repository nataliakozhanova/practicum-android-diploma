package ru.practicum.android.diploma.filters.settingsfilters.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filters.settingsfilters.domain.api.SettingsInteractor
import ru.practicum.android.diploma.filters.settingsfilters.domain.models.SalaryFilters

class SettingsFiltersViewModel(
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    private var salaryFilters = settingsInteractor.getSalaryFilters()

    private val _state = MutableLiveData<SalaryFilters?>()
    fun observeFilters(): MutableLiveData<SalaryFilters?> = _state

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

}
