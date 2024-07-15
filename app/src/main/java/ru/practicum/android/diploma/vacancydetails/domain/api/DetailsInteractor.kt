package ru.practicum.android.diploma.vacancydetails.domain.api

import android.content.Intent
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.data.ErrorType
import ru.practicum.android.diploma.vacancydetails.domain.models.VacancyDetails

interface DetailsInteractor {
    fun getVacancyDetail(vacancyId: String): Flow<Pair<VacancyDetails?, ErrorType>>
    fun shareVacancy(hhVacancyLink: String): Intent
}
