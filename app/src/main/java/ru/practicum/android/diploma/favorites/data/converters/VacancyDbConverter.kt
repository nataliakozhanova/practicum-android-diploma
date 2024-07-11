package ru.practicum.android.diploma.favorites.data.converters

import ru.practicum.android.diploma.common.domain.EmployerInfo
import ru.practicum.android.diploma.common.domain.SalaryInfo
import ru.practicum.android.diploma.common.domain.VacancyBase
import ru.practicum.android.diploma.favorites.data.db.VacancyEntity

class VacancyDbConverter {
    fun map(vacancy: VacancyBase): VacancyEntity {
        return VacancyEntity(
            vacancy.hhID,
            vacancy.name,
            vacancy.isFavorite,
            vacancy.employerInfo.areaName,
            vacancy.employerInfo.employerName,
            vacancy.employerInfo.employerLogoUrl ?: "",
            vacancy.salaryInfo?.salaryTo ?: 0,
            vacancy.salaryInfo?.salaryFrom ?: 0,
            vacancy.salaryInfo?.salaryCurrency ?: ""
        )
    }

    fun map(vacancy: VacancyEntity): VacancyBase {
        return VacancyBase(
            hhID = vacancy.hhID,
            name = vacancy.name,
            isFavorite = vacancy.isFavorite,
            employerInfo = EmployerInfo(
                employerName = vacancy.employerName,
                employerLogoUrl = vacancy.employerLogoUrl,
                areaName = vacancy.areaName
            ),
            salaryInfo = SalaryInfo(
                salaryFrom = if (vacancy.salaryFrom > 0) vacancy.salaryFrom else null,
                salaryTo = if (vacancy.salaryTo > 0) vacancy.salaryTo else null,
                salaryCurrency = if (vacancy.salaryCurrency.isNotEmpty()) vacancy.salaryCurrency else null
            )
        )
    }
}
