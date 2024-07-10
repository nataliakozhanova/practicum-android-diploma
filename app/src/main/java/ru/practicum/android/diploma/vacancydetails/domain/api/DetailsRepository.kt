package ru.practicum.android.diploma.vacancydetails.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.Resource
import ru.practicum.android.diploma.vacancydetails.domain.DetailsResult

interface DetailsRepository {
    fun getVacancyDetails(vacancyId: String): Flow<Resource<DetailsResult?>>
}
