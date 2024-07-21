package ru.practicum.android.diploma.filters.chooseindustry.domain.interfaces

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.data.ErrorType
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustriesResult
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustriesModel

interface IndustryInteractor {
    fun getIndustries(): Flow<Pair<IndustriesResult?, ErrorType>>
    fun saveIndustrySettings(industry: IndustriesModel)
    fun getIndustrySettings(): IndustriesModel?
    fun deleteIndustrySettings()
}
