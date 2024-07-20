package ru.practicum.android.diploma.filters.choosearea.data.storage

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.filters.choosearea.domain.models.AreaInfo

const val AREA_SETTINGS_KEY = "key_for_area_settings"

class AreasStorageImpl(private val sharedPreferences: SharedPreferences) :
    AreasStorageApi {

    override fun readArea(): AreaInfo? {
        val json = sharedPreferences.getString(AREA_SETTINGS_KEY, null)
        if (json != null) return Gson().fromJson(json, AreaInfo::class.java)
        return null
    }

    override fun writeArea(area: AreaInfo) {
        val json = Gson().toJson(area)
        sharedPreferences.edit()
            .putString(AREA_SETTINGS_KEY, json)
            .apply()
    }

    override fun removeArea() {
        sharedPreferences.edit()
            .remove(AREA_SETTINGS_KEY)
            .apply()
    }
}
