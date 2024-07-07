package ru.practicum.android.diploma.search.domain.models

import ru.practicum.android.diploma.common.domain.EmployerInfo
import ru.practicum.android.diploma.common.domain.SalaryInfo
import ru.practicum.android.diploma.common.domain.VacancyBase

class VacancySearchResultItem(
    id: Int,
    hhID: String,
    name: String,
    isFavorite: Boolean,
    val employerInfo: EmployerInfo,
    val salaryInfo: SalaryInfo
) : VacancyBase(
    id,
    hhID,
    name,
    isFavorite
)
