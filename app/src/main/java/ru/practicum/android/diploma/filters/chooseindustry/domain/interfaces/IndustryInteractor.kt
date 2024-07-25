package ru.practicum.android.diploma.filters.chooseindustry.domain.interfaces

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.ErrorType
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustriesModel
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustriesResult

interface IndustryInteractor {
    fun getIndustries(): Flow<Pair<IndustriesResult?, ErrorType>>
    fun saveIndustrySettings(industry: IndustriesModel)
    fun getIndustrySettings(): IndustriesModel?
    fun deleteIndustrySettings()
    fun searchIndustries(query: String): Flow<Pair<IndustriesResult?, ErrorType>>
}
