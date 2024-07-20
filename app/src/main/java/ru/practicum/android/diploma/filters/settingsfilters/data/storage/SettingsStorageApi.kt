package ru.practicum.android.diploma.filters.settingsfilters.data.storage

import ru.practicum.android.diploma.filters.settingsfilters.domain.models.SalaryFilters

interface SettingsStorageApi {
    fun readSalaryFilters(): SalaryFilters?
    fun writeSalaryFilters(salaryFilters: SalaryFilters)
    fun removeSalaryFilters()
}
