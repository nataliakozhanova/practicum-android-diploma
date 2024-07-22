package ru.practicum.android.diploma.filters.chooseindustry.domain.interfaces

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.data.Resource
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustriesModel
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustriesResult

interface IndustryRepository {
    fun getIndustries(): Flow<Resource<IndustriesResult>>
    fun saveIndustrySettings(industry: IndustriesModel)
    fun getIndustrySettings(): IndustriesModel?
    fun deleteIndustrySettings()
    fun searchIndustries(query: String): Flow<Resource<IndustriesResult>>
}
