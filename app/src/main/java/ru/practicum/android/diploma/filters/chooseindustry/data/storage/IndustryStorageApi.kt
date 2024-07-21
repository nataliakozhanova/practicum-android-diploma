package ru.practicum.android.diploma.filters.chooseindustry.data.storage

import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustriesModel

interface IndustryStorageApi {
    fun readIndustry(): IndustriesModel?
    fun writeIndustry(industry: IndustriesModel)
    fun removeIndustry()
}
