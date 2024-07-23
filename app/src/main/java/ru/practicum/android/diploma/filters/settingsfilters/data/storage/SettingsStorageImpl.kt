package ru.practicum.android.diploma.filters.settingsfilters.data.storage

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.common.domain.FiltersAll
import ru.practicum.android.diploma.filters.settingsfilters.domain.models.SalaryFilters

const val SALARY_FILTERS_KEY = "key_for_salary_filters"
const val PREVIOUS_FILTERS_KEY = "key_for_previous_filters"

class SettingsStorageImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
) :
    SettingsStorageApi {
    override fun readSalaryFilters(): SalaryFilters? {
        val json = sharedPreferences.getString(SALARY_FILTERS_KEY, null)
        if (json != null) return gson.fromJson(json, SalaryFilters::class.java)
        return null
    }

    override fun writeSalaryFilters(salaryFilters: SalaryFilters) {
        val json = gson.toJson(salaryFilters)
        sharedPreferences.edit()
            .putString(SALARY_FILTERS_KEY, json)
            .apply()
    }

    override fun removeSalaryFilters() {
        sharedPreferences.edit()
            .remove(SALARY_FILTERS_KEY)
            .apply()
    }

    override fun readPreviousFilters(): FiltersAll? {
        val json = sharedPreferences.getString(PREVIOUS_FILTERS_KEY, null)
        if (json != null) return gson.fromJson(json, FiltersAll::class.java)
        return null
    }

    override fun writePreviousFilters(filters: FiltersAll) {
        val json = gson.toJson(filters)
        sharedPreferences.edit()
            .putString(PREVIOUS_FILTERS_KEY, json)
            .apply()
    }

    override fun removePreviousFilters() {
        sharedPreferences.edit()
            .remove(PREVIOUS_FILTERS_KEY)
            .apply()
    }
}
