package ru.practicum.android.diploma.vacancydetails.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.data.ErrorType
import ru.practicum.android.diploma.common.data.Resource
import ru.practicum.android.diploma.vacancydetails.domain.models.DetailsResult
import ru.practicum.android.diploma.vacancydetails.domain.api.DetailsInteractor
import ru.practicum.android.diploma.vacancydetails.domain.api.DetailsRepository

class DetailsInteractorImpl(private val repository: DetailsRepository) : DetailsInteractor {
    override fun getVacancyDetail(vacancyId: String): Flow<Pair<DetailsResult?, ErrorType>> {
        return repository.getVacancyDetails(vacancyId).map { result ->
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
}
