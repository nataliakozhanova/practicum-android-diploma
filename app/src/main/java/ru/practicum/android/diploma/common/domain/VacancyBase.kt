package ru.practicum.android.diploma.common.domain

open class VacancyBase(
    val hhID: String,
    val name: String,
    val area: String,
    val isFavorite: Boolean,
    val employerInfo: EmployerInfo,
    val salaryInfo: SalaryInfo?,
)
