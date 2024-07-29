package ru.practicum.android.diploma.filters.choosearea.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.ErrorType
import ru.practicum.android.diploma.filters.choosearea.domain.models.AreaInfo
import ru.practicum.android.diploma.filters.choosearea.domain.models.AreasResult
import ru.practicum.android.diploma.filters.choosearea.domain.models.CountriesResult

interface ChooseAreaInteractor {
    fun getCountries(): Flow<Pair<CountriesResult?, ErrorType>>
    fun getAreaByParentId(areaId: String): Flow<Pair<AreasResult?, ErrorType>>
    fun getAreasWithCountries(): Flow<Pair<AreasResult?, ErrorType>>
    fun saveAreaSettings(area: AreaInfo)
    fun getAreaSettings(): AreaInfo?
    fun deleteAreaSettings()
    fun savePreviousAreaSettings(area: AreaInfo)
    fun getPreviousAreaSettings(): AreaInfo?
    fun deletePreviousAreaSettings()
}
