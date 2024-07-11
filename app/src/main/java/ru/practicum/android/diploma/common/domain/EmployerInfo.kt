package ru.practicum.android.diploma.common.domain

data class EmployerInfo(
    val employerName: String,
    val employerLogoUrl: String?,
    // val areaName: String - добавлено отдельным полем в VacancyBase
)
