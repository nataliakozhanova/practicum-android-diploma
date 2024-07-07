package ru.practicum.android.diploma.search.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.common.data.NetworkClient
import ru.practicum.android.diploma.common.domain.Resource
import ru.practicum.android.diploma.search.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.vacancydetails.presentation.models.Vacancy

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
) : SearchRepository {
    override fun findVacancies(expression: String, page: Int?, perPage: Int?): Flow<Resource<List<Vacancy>>> = flow {
        when (val response = networkClient.doRequest(VacancySearchRequest(expression, page, perPage))) {
            is VacancySearchResponse -> {
                emit(Resource.Success(response.items.map {
                    Vacancy(
                        id = it.id,
                        name = it.name,
                        region = it.area.name,
                        city = it.address?.city,
                        companyId = it.employer.id,
                        companyName = it.employer.name,
                        companyLogo = it.employer.logoUrls?.logo240,
                        salaryCurrency = it.salary.currency,
                        salaryFrom = it.salary.from,
                        salaryTo = it.salary.to,
                        employment = it.employment.name,
                        experience = it.experience.name,
                    )
                }))
            }
            else -> {
                emit(Resource.Error(response.errorType))
            }
        }
    }
}
