package ru.practicum.android.diploma.filters.settingsfilters.domain.api

import ru.practicum.android.diploma.common.domain.FiltersAll
import ru.practicum.android.diploma.filters.settingsfilters.domain.models.SalaryFilters

interface SettingsRepository {
    fun getSalaryFilters(): SalaryFilters?
    fun saveSalaryFilters(salaryFilters: SalaryFilters)
    fun deleteSalaryFilters()
    fun getStashedFilters(): FiltersAll?
    fun saveStashedFilters(filters: FiltersAll)
    fun deleteStashedFilters()
}
