package ru.practicum.android.diploma.filters.settingsfilters.domain.api

import ru.practicum.android.diploma.filters.settingsfilters.domain.models.SalaryFilters

interface SettingsInteractor {
    fun getSalaryFilters(): SalaryFilters?
    fun saveSalaryFilters(salaryFilters: SalaryFilters)
    fun deleteSalaryFilters()
}
