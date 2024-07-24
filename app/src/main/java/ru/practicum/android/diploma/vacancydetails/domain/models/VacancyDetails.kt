package ru.practicum.android.diploma.vacancydetails.domain.models

import ru.practicum.android.diploma.common.domain.EmployerInfo
import ru.practicum.android.diploma.common.domain.NameInfo
import ru.practicum.android.diploma.common.domain.SalaryInfo
import ru.practicum.android.diploma.common.domain.VacancyBase

class VacancyDetails(
    hhID: String,
    name: String,
    isFavorite: Boolean,
    employerInfo: EmployerInfo, // name, logoUrl, areaName
    salaryInfo: SalaryInfo?,
    val details: Details,
) : VacancyBase(
    hhID,
    name,
    isFavorite,
    employerInfo,
    salaryInfo
)

data class Details(
    val address: Address?,
    val experience: NameInfo?,
    val employment: NameInfo?,
    val schedule: NameInfo?,
    val description: String,
    val keySkills: List<NameInfo>,
    val hhVacancyLink: String,
)

data class Address(
    val city: String?,
    val building: String?,
    val street: String?,
    val description: String?,
)
