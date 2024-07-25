package ru.practicum.android.diploma.search.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.common.data.NetworkClient
import ru.practicum.android.diploma.common.domain.Resource
import ru.practicum.android.diploma.common.domain.EmployerInfo
import ru.practicum.android.diploma.common.domain.SalaryInfo
import ru.practicum.android.diploma.common.domain.VacancyBase
import ru.practicum.android.diploma.search.data.dto.VacancySearchDto
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.models.SearchResult
import ru.practicum.android.diploma.search.domain.models.VacancySearchRequest

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
) : SearchRepository {
    override fun findVacancies(searchRequest: VacancySearchRequest): Flow<Resource<SearchResult?>> = flow {
        when (val response = networkClient.doRequest(searchRequest)) {
            is VacancySearchResponse -> {
                emit(
                    Resource
                        .Success(
                            SearchResult(
                                page = response.page,
                                perPage = response.perPage,
                                pages = response.pages,
                                found = response.found,
                                vacancies = response.items.map(::convertVacancy)
                            )
                        )
                )
            }

            else -> {
                emit(Resource.Error(response.errorType))
            }
        }
    }

    private fun convertVacancy(it: VacancySearchDto): VacancyBase =
        VacancyBase(
            hhID = it.id,
            name = it.name,
            isFavorite = false,
            employerInfo = EmployerInfo(
                areaName = it.area.name,
                employerName = it.employer.name,
                employerLogoUrl = it.employer.logoUrls?.logo240
            ),
            salaryInfo = if (it.salary != null) {
                SalaryInfo(
                    salaryFrom = it.salary.from,
                    salaryTo = it.salary.to,
                    salaryCurrency = it.salary.currency
                )
            } else {
                null
            }
        )

}
