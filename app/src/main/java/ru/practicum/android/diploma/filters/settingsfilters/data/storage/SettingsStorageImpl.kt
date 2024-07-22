package ru.practicum.android.diploma.filters.settingsfilters.data.storage

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.common.domain.FiltersAll
import ru.practicum.android.diploma.filters.settingsfilters.domain.models.SalaryFilters

const val SALARY_FILTERS_KEY = "key_for_salary_filters"
const val STASHED_FILTERS_KEY = "key_for_stashed_filters"

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

    override fun readStashedFilters(): FiltersAll? {
        val json = sharedPreferences.getString(STASHED_FILTERS_KEY, null)
        if (json != null) return gson.fromJson(json, FiltersAll::class.java)
        return null
    }

    override fun writeStashedFilters(filters: FiltersAll) {
        val json = gson.toJson(filters)
        sharedPreferences.edit()
            .putString(STASHED_FILTERS_KEY, json)
            .apply()
    }

    override fun removeStashedFilters() {
        sharedPreferences.edit()
            .remove(STASHED_FILTERS_KEY)
            .apply()
    }
}
