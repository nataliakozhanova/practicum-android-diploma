package ru.practicum.android.diploma.search.domain.models

import ru.practicum.android.diploma.common.domain.VacancyBase

class VacancySearchResultItem(
    id: Int,
    hhID: String,
    name: String,
    isFavorite: Boolean,
    val employerName: String,
    val employerLogoUrl: String,
    val salaryFrom: Int,
    val salaryTo: Int?,
    val salaryCurrency: String,
    val areaName: String,
) : VacancyBase(
    id,
    hhID,
    name,
    isFavorite
)
