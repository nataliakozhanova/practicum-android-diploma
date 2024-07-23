package ru.practicum.android.diploma.filters.chooseindustry.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.domain.ErrorType
import ru.practicum.android.diploma.common.domain.Resource
import ru.practicum.android.diploma.filters.chooseindustry.domain.interfaces.IndustryInteractor
import ru.practicum.android.diploma.filters.chooseindustry.domain.interfaces.IndustryRepository
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustriesModel
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustriesResult

class IndustryInteractorImpl(
    private val repository: IndustryRepository
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
        repository.saveIndustrySettings(industry)
    }

    override fun getIndustrySettings(): IndustriesModel? {
        return repository.getIndustrySettings()
    }

    override fun deleteIndustrySettings() {
        repository.deleteIndustrySettings()
    }
}
