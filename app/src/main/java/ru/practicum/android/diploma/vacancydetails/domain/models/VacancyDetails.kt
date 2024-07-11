package ru.practicum.android.diploma.vacancydetails.domain.models

import ru.practicum.android.diploma.common.domain.EmployerInfo
import ru.practicum.android.diploma.common.domain.NameInfo
import ru.practicum.android.diploma.common.domain.SalaryInfo
import ru.practicum.android.diploma.common.domain.VacancyBase

class VacancyDetails(
    hhID: String,
    name: String,
    area: String,
    isFavorite: Boolean,
    employerInfo: EmployerInfo, //name, logoUrl
    salaryInfo: SalaryInfo?,
    val address: Address?,
    val experience: NameInfo?,
    val employment: NameInfo?,
    val schedule: NameInfo?,
    val description: String,
    val keySkill: List<String>,
    val contacts: Contacts?,
    val hhVacancyLink: String,
) : VacancyBase(
    hhID, name, area, isFavorite, employerInfo, salaryInfo
)

data class Address(
    val city: String?,
    val building: String?,
    val street: String?,
    val description: String?,
)

data class Contacts(
    val email: String?,
    val name: String?,
    val phone: List<Phone>?,
)

data class Phone(
    val city: String,
    val comment: String?,
    val country: String,
    val formatted: String,
    val number: String,
)
