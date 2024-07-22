package ru.practicum.android.diploma.filters.chooseindustry.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.common.data.NetworkClient
import ru.practicum.android.diploma.common.data.Resource
import ru.practicum.android.diploma.filters.chooseindustry.data.dto.IndustrtesRequest
import ru.practicum.android.diploma.filters.chooseindustry.data.dto.IndustryDto
import ru.practicum.android.diploma.filters.chooseindustry.data.dto.IndustryResponse
import ru.practicum.android.diploma.filters.chooseindustry.data.storage.IndustryStorageApi
import ru.practicum.android.diploma.filters.chooseindustry.domain.interfaces.IndustryRepository
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustriesModel

class IndustryRepositoryImpl(
    private val networkClient: NetworkClient,
    private val storage: IndustryStorageApi
) : IndustryRepository {
    override fun getIndustries(): Flow<Resource<List<IndustriesModel>>> = flow {
        when (val response = networkClient.doRequest(IndustrtesRequest())) {
            is IndustryResponse -> {
                val industryItems = response.industries.flatMap { convertIndustry(it) }
                emit(Resource.Success(industryItems.sortedBy { it.name }))
            }
            else -> {
                emit(Resource.Error(response.errorType))
            }
        }
    }

    override fun searchIndustries(query: String): Flow<Resource<List<IndustriesModel>>> = flow {
        when (val response = networkClient.doRequest(IndustrtesRequest(query))) {
            is IndustryResponse -> {
                val industryItems = response.industries.flatMap { convertIndustry(it) }
                emit(Resource.Success(industryItems.sortedBy { it.name }))
            }
            else -> {
                emit(Resource.Error(response.errorType))
            }
        }
    }


    private fun convertIndustry(it: IndustryDto): List<IndustriesModel> {
        val industryItems = mutableListOf<IndustriesModel>()
        industryItems.add(IndustriesModel(it.id, it.name))
        it.industries?.forEach { subIndustry ->
            industryItems.add(IndustriesModel(subIndustry.id ?: "", subIndustry.name ?: ""))
        }
        return industryItems
    }

    override fun saveIndustrySettings(industry: IndustriesModel) {
        storage.writeIndustry(industry)
    }

    override fun getIndustrySettings(): IndustriesModel? {
        return storage.readIndustry()
    }

    override fun deleteIndustrySettings() {
        storage.removeIndustry()
    }
}
