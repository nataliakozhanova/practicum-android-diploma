package ru.practicum.android.diploma.vacancydetails.domain.impl

import android.content.Intent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.data.ErrorType
import ru.practicum.android.diploma.common.data.Resource
import ru.practicum.android.diploma.vacancydetails.domain.api.DetailsInteractor
import ru.practicum.android.diploma.vacancydetails.domain.api.DetailsRepository
import ru.practicum.android.diploma.vacancydetails.domain.api.ExternalNavigator
import ru.practicum.android.diploma.vacancydetails.domain.models.VacancyDetails

class DetailsInteractorImpl(
    private val repository: DetailsRepository,
    private val externalNavigator: ExternalNavigator
) : DetailsInteractor {
    override fun getVacancyDetail(vacancyId: String): Flow<Pair<VacancyDetails?, ErrorType>> {
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

    override fun shareVacancy(hhVacancyLink: String): Intent {
        return externalNavigator.shareVacancy(hhVacancyLink)
    }
}
