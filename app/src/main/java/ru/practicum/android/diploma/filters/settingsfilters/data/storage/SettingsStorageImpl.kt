package ru.practicum.android.diploma.filters.settingsfilters.data.storage

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.filters.settingsfilters.domain.models.SalaryFilters

const val SALARY_FILTERS_KEY = "key_for_salary_filters"

class SettingsStorageImpl(private val sharedPreferences: SharedPreferences) :
    SettingsStorageApi {
    override fun readSalaryFilters(): SalaryFilters? {
        val json = sharedPreferences.getString(SALARY_FILTERS_KEY, null)
        if (json != null) return Gson().fromJson(json, SalaryFilters::class.java)
        return null
    }

    override fun writeSalaryFilters(salaryFilters: SalaryFilters) {
        val json = Gson().toJson(salaryFilters)
        sharedPreferences.edit()
            .putString(SALARY_FILTERS_KEY, json)
            .apply()
    }

    override fun removeSalaryFilters() {
        sharedPreferences.edit()
            .remove(SALARY_FILTERS_KEY)
            .apply()
    }
}
