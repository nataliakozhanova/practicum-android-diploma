package ru.practicum.android.diploma.filters.settingsfilters.data.storage

import ru.practicum.android.diploma.common.domain.FiltersAll
import ru.practicum.android.diploma.filters.settingsfilters.domain.models.SalaryFilters

interface SettingsStorageApi {
    fun readSalaryFilters(): SalaryFilters?
    fun writeSalaryFilters(salaryFilters: SalaryFilters)
    fun removeSalaryFilters()
    fun readPreviousFilters(): FiltersAll?
    fun writePreviousFilters(filters: FiltersAll)
    fun removePreviousFilters()
}
