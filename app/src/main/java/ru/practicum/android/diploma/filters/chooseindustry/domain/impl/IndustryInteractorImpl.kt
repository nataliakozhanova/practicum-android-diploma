package ru.practicum.android.diploma.filters.chooseindustry.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.data.ErrorType
import ru.practicum.android.diploma.common.data.Resource
import ru.practicum.android.diploma.filters.chooseindustry.data.storage.IndustryStorageApi
import ru.practicum.android.diploma.filters.chooseindustry.domain.interfaces.IndustryInteractor
import ru.practicum.android.diploma.filters.chooseindustry.domain.interfaces.IndustryRepository
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustriesModel
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustriesResult

class IndustryInteractorImpl(
    private val repository: IndustryRepository,
    private val industryStorageApi: IndustryStorageApi
) : IndustryInteractor {
    override fun getIndustries(): Flow<Pair<IndustriesResult?, ErrorType>> {
        return repository.getIndustries().map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data?.let { IndustriesResult(it) }, result.error)
                }
                is Resource.Error -> {
                    Pair(null, result.error)
                }
            }
        }
    }

    override fun searchIndustries(query: String): Flow<Pair<IndustriesResult?, ErrorType>> {
        return repository.searchIndustries(query).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data?.let { IndustriesResult(it) }, result.error)
                }
                is Resource.Error -> {
                    Pair(null, result.error)
                }
            }
        }
    }

    override fun saveIndustrySettings(industry: IndustriesModel) {
        industryStorageApi.writeIndustry(industry)
    }

    override fun getIndustrySettings(): IndustriesModel? {
        return industryStorageApi.readIndustry()
    }

    override fun deleteIndustrySettings() {
        industryStorageApi.removeIndustry()
    }
}
