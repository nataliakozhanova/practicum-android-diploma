package ru.practicum.android.diploma.filters.chooseindustry.data.storage

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustriesModel

const val INDUSTRY_FILTERS_KEY = "key_for_industry_filters"

class IndustryStorageImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
) : IndustryStorageApi {

    override fun readIndustry(): IndustriesModel? {
        val json = sharedPreferences.getString(INDUSTRY_FILTERS_KEY, null)
        if (json != null) return gson.fromJson(json, IndustriesModel::class.java)
        return null
    }

    override fun writeIndustry(industry: IndustriesModel) {
        val json = gson.toJson(industry)
        sharedPreferences.edit()
            .putString(INDUSTRY_FILTERS_KEY, json)
            .apply()
    }

    override fun removeIndustry() {
        sharedPreferences.edit()
            .remove(INDUSTRY_FILTERS_KEY)
            .apply()
    }
}
