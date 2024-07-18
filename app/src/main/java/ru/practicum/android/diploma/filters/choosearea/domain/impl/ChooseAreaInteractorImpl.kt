package ru.practicum.android.diploma.filters.choosearea.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.data.ErrorType
import ru.practicum.android.diploma.common.data.Resource
import ru.practicum.android.diploma.filters.choosearea.domain.api.ChooseAreaInteractor
import ru.practicum.android.diploma.filters.choosearea.domain.api.ChooseAreaRepository
import ru.practicum.android.diploma.filters.choosearea.domain.models.AreasResult
import ru.practicum.android.diploma.filters.choosearea.domain.models.CountriesResult

class ChooseAreaInteractorImpl(private val repository: ChooseAreaRepository) : ChooseAreaInteractor {
    override fun getCountries(): Flow<Pair<CountriesResult?, ErrorType>> {
        return repository.getCountries().map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, result.error)
                }

                is Resource.Error -> {
                    Pair(null, result.error)
                }
            }
        }
    }

    override fun getAreaByParentId(areaId: String): Flow<Pair<AreasResult?, ErrorType>> {
        return repository.getAreaByParentId(areaId).map { mapResult(it) }
    }

    override fun getAreasWithCountry(): Flow<Pair<AreasResult?, ErrorType>> {
        return repository.getAreasWithCountry().map { mapResult(it) }
    }

    private fun mapResult(result: Resource<AreasResult?>): Pair<AreasResult?, ErrorType> {
        return when (result) {
            is Resource.Success -> {
                Pair(result.data, result.error)
            }

            is Resource.Error -> {
                Pair(null, result.error)
            }
        }
    }
}
