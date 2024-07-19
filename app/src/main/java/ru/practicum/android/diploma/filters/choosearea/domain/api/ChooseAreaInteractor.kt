package ru.practicum.android.diploma.filters.choosearea.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.data.ErrorType
import ru.practicum.android.diploma.filters.choosearea.domain.models.AreasResult
import ru.practicum.android.diploma.filters.choosearea.domain.models.CountriesResult

interface ChooseAreaInteractor {
    fun getCountries(): Flow<Pair<CountriesResult?, ErrorType>>
    fun getAreaByParentId(areaId: String): Flow<Pair<AreasResult?, ErrorType>>
    fun getAreasWithCountries(): Flow<Pair<AreasResult?, ErrorType>>
}
