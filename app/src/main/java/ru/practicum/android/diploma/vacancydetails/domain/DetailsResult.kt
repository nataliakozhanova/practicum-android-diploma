package ru.practicum.android.diploma.vacancydetails.domain

import ru.practicum.android.diploma.common.domain.VacancyBase

class DetailsResult(
    val page: Int,
    val perPage: Int,
    val pages: Int,
    val found: Int,
    val vacancies: List<VacancyBase>
)
