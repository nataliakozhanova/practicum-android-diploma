package ru.practicum.android.diploma.vacancydetails.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.common.data.NetworkClient
import ru.practicum.android.diploma.common.domain.EmployerInfo
import ru.practicum.android.diploma.common.domain.Resource
import ru.practicum.android.diploma.common.domain.SalaryInfo
import ru.practicum.android.diploma.common.domain.VacancyBase
import ru.practicum.android.diploma.search.data.dto.VacancyDto
import ru.practicum.android.diploma.vacancydetails.data.dto.DetailsRequest
import ru.practicum.android.diploma.vacancydetails.data.dto.DetailsResponse
import ru.practicum.android.diploma.vacancydetails.domain.DetailsResult
import ru.practicum.android.diploma.vacancydetails.domain.api.DetailsRepository

class DetailsRepositoryImpl(private val networkClient: NetworkClient) : DetailsRepository {
    override fun getVacancyDetails(vacancyId: String): Flow<Resource<DetailsResult?>> = flow {
        when (val responce = networkClient.doRequest(DetailsRequest(vacancyId))) {
            is DetailsResponse -> {
                emit(
                    Resource.Success(
                        DetailsResult(
                            pages = responce.pages,
                            perPage = responce.perPage,
                            page = responce.page,
                            found = responce.found,
                            vacancies = responce.items.map(::mapDetail)
                        )
                    )
                )
            }
            else -> emit(Resource.Error(responce.errorType))
        }
    }

    private fun mapDetail(it: VacancyDto): VacancyBase = VacancyBase(
        hhID = it.id,
        name = it.name,
        area = it.area.name,
        isFavorite = false,
        employerInfo = EmployerInfo(
            employerName = it.employer.name,
            employerLogoUrl = it.employer.logoUrls?.logo240,
        ),
        salaryInfo = if (it.salary != null) {
            SalaryInfo(
                salaryCurrency = it.salary.currency,
                salaryTo = it.salary.to,
                salaryFrom = it.salary.from
            )
        } else {
            null
        }
    )
}
