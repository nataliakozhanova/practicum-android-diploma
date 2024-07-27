package ru.practicum.android.diploma.filters.choosearea.data.storage

import ru.practicum.android.diploma.filters.choosearea.domain.models.AreaInfo

interface AreasStorageApi {
    fun readArea(): AreaInfo?
    fun writeArea(area: AreaInfo)
    fun removeArea()
    fun readPreviousArea(): AreaInfo?
    fun writePreviousArea(area: AreaInfo)
    fun removePreviousArea()
}
