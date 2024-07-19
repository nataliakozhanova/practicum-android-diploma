package ru.practicum.android.diploma.filters.choosearea.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.data.Resource
import ru.practicum.android.diploma.filters.choosearea.domain.models.AreasResult
import ru.practicum.android.diploma.filters.choosearea.domain.models.CountriesResult

interface ChooseAreaRepository {
    fun getCountries(): Flow<Resource<CountriesResult?>>
    fun getAreaByParentId(areaId: String): Flow<Resource<AreasResult?>>
    fun getAreasWithCountries(): Flow<Resource<AreasResult?>>
}
