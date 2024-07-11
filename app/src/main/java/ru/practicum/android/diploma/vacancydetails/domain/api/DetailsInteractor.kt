package ru.practicum.android.diploma.vacancydetails.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.ErrorType
import ru.practicum.android.diploma.vacancydetails.domain.DetailsResult

interface DetailsInteractor {
    fun getVacancyDetail(vacancyId: String): Flow<Pair<DetailsResult?, ErrorType>>
}
