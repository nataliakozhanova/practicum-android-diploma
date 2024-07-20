package ru.practicum.android.diploma.filters.choosearea.data.storage

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.filters.choosearea.domain.models.AreaInfo

const val AREA_FILTERS_KEY = "key_for_area_filters"

class AreasStorageImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
) :
    AreasStorageApi {

    override fun readArea(): AreaInfo? {
        val json = sharedPreferences.getString(AREA_FILTERS_KEY, null)
        if (json != null) return gson.fromJson(json, AreaInfo::class.java)
        return null
    }

    override fun writeArea(area: AreaInfo) {
        val json = gson.toJson(area)
        sharedPreferences.edit()
            .putString(AREA_FILTERS_KEY, json)
            .apply()
    }

    override fun removeArea() {
        sharedPreferences.edit()
            .remove(AREA_FILTERS_KEY)
            .apply()
    }
}
