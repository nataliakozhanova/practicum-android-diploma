package ru.practicum.android.diploma.vacancydetails.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.common.data.NetworkClient
import ru.practicum.android.diploma.common.data.Resource
import ru.practicum.android.diploma.common.domain.EmployerInfo
import ru.practicum.android.diploma.common.domain.SalaryInfo
import ru.practicum.android.diploma.common.domain.VacancyBase
import ru.practicum.android.diploma.search.data.dto.VacancySearchDto
import ru.practicum.android.diploma.vacancydetails.data.dto.VacancyDetailsRequest
import ru.practicum.android.diploma.vacancydetails.data.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.vacancydetails.domain.api.DetailsRepository
import ru.practicum.android.diploma.vacancydetails.domain.models.DetailsResult

class DetailsRepositoryImpl(private val networkClient: NetworkClient) : DetailsRepository {
    override fun getVacancyDetails(vacancyId: String): Flow<Resource<DetailsResult?>> = flow {
        when (val response = networkClient.doRequest(VacancyDetailsRequest(vacancyId))) {
            is VacancyDetailsResponse -> {
                emit(
                    Resource.Success(DetailsResult(response.id)) // Здесь нужно привеcти сразу к модели domain VacancyDetails.
                )
            }

            else -> {
                emit(Resource.Error(response.errorType))
            }
        }
    }

    private fun mapDetail(it: VacancySearchDto): VacancyBase =
        VacancyBase( // Оставлено для образца. Можно сделать конвертацию из VacancyDetailsResponse (dto) в VacancyDetails(domain)
            hhID = it.id,
            name = it.name,
            isFavorite = false,
            employerInfo = EmployerInfo(
                employerName = it.employer.name,
                employerLogoUrl = it.employer.logoUrls?.logo240,
                areaName = it.area.name
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
