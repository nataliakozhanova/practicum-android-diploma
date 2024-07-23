package ru.practicum.android.diploma.filters.settingsfilters.domain.impl

import ru.practicum.android.diploma.common.domain.FiltersAll
import ru.practicum.android.diploma.filters.settingsfilters.domain.api.SettingsInteractor
import ru.practicum.android.diploma.filters.settingsfilters.domain.api.SettingsRepository
import ru.practicum.android.diploma.filters.settingsfilters.domain.models.SalaryFilters

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {
    override fun getSalaryFilters(): SalaryFilters? {
        return repository.getSalaryFilters()
    }

    override fun saveSalaryFilters(salaryFilters: SalaryFilters) {
        repository.saveSalaryFilters(salaryFilters)
    }

    override fun deleteSalaryFilters() {
        repository.deleteSalaryFilters()
    }

    override fun getPreviousFilters(): FiltersAll? = repository.getPreviousFilters()

    override fun savePreviousFilters(filters: FiltersAll?) {
        if (filters != null) {
            repository.savePreviousFilters(filters)
        } else {
            repository.deletePreviousFilters()
        }
    }

    override fun deletePreviousFilters() {
        repository.deletePreviousFilters()
    }
}
