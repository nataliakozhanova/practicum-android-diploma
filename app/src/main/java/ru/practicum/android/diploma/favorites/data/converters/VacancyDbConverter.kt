package ru.practicum.android.diploma.favorites.data.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.common.domain.EmployerInfo
import ru.practicum.android.diploma.common.domain.NameInfo
import ru.practicum.android.diploma.common.domain.SalaryInfo
import ru.practicum.android.diploma.vacancydetails.domain.models.Phone
import ru.practicum.android.diploma.favorites.data.db.VacancyEntity
import ru.practicum.android.diploma.vacancydetails.domain.models.Address
import ru.practicum.android.diploma.vacancydetails.domain.models.Contacts
import ru.practicum.android.diploma.vacancydetails.domain.models.Details
import ru.practicum.android.diploma.vacancydetails.domain.models.VacancyDetails

class VacancyDbConverter {
    fun map(vacancy: VacancyDetails): VacancyEntity {
        return VacancyEntity(
            hhID = vacancy.hhID,
            name = vacancy.name,
            isFavorite = vacancy.isFavorite,
            areaName = vacancy.employerInfo.areaName,
            employerName = vacancy.employerInfo.employerName,
            employerLogoUrl = vacancy.employerInfo.employerLogoUrl ?: "",
            salaryTo = vacancy.salaryInfo?.salaryTo ?: 0,
            salaryFrom = vacancy.salaryInfo?.salaryFrom ?: 0,
            salaryCurrency = vacancy.salaryInfo?.salaryCurrency ?: "",
            addressCity = vacancy.details.address?.city,
            addressBuilding = vacancy.details.address?.building,
            addressStreet = vacancy.details.address?.street,
            addressDescription = vacancy.details.address?.description,
            experienceId = vacancy.details.experience?.id,
            experienceName = vacancy.details.experience?.name,
            employmentId = vacancy.details.employment?.id,
            employmentName = vacancy.details.employment?.name,
            scheduleId = vacancy.details.schedule?.id,
            scheduleName = vacancy.details.schedule?.name,
            description = vacancy.details.description,
            keySkills = Gson().toJson(vacancy.details.keySkill),
            contactEmail = vacancy.details.contacts?.email,
            contactName = vacancy.details.contacts?.name,
            contactPhoneCity = vacancy.details.contacts?.phone?.firstOrNull()?.city,
            contactPhoneComment = vacancy.details.contacts?.phone?.firstOrNull()?.comment,
            contactPhoneCountry = vacancy.details.contacts?.phone?.firstOrNull()?.country,
            contactPhoneFormatted = vacancy.details.contacts?.phone?.firstOrNull()?.formatted,
            contactPhoneNumber = vacancy.details.contacts?.phone?.firstOrNull()?.number,
            hhVacancyLink = vacancy.details.hhVacancyLink
        )
    }

    fun map(vacancy: VacancyEntity): VacancyDetails {
        return VacancyDetails(
            hhID = vacancy.hhID,
            name = vacancy.name,
            isFavorite = vacancy.isFavorite,
            employerInfo = EmployerInfo(
                areaName = vacancy.areaName,
                employerName = vacancy.employerName,
                employerLogoUrl = vacancy.employerLogoUrl,
            ),
            salaryInfo = SalaryInfo(
                salaryTo = vacancy.salaryTo,
                salaryFrom = vacancy.salaryFrom,
                salaryCurrency = vacancy.salaryCurrency
            ),
            details = Details(
                address = Address(
                    city = vacancy.addressCity,
                    building = vacancy.addressBuilding,
                    street = vacancy.addressStreet,
                    description = vacancy.addressDescription
                ),
                experience = NameInfo(
                    id = vacancy.experienceId ?: "",
                    name = vacancy.experienceName ?: ""
                ),
                employment = NameInfo(
                    id = vacancy.employmentId ?: "",
                    name = vacancy.employmentName ?: ""
                ),
                schedule = NameInfo(
                    id = vacancy.scheduleId ?: "",
                    name = vacancy.scheduleName ?: ""
                ),
                description = vacancy.description,
                keySkill = Gson().fromJson(vacancy.keySkills, object : TypeToken<List<String>>() {}.type),
                contacts = Contacts(
                    email = vacancy.contactEmail,
                    name = vacancy.contactName,
                    phone = listOf(
                        Phone(
                            city = vacancy.contactPhoneCity ?: "",
                            comment = vacancy.contactPhoneComment,
                            country = vacancy.contactPhoneCountry ?: "",
                            formatted = vacancy.contactPhoneFormatted ?: "",
                            number = vacancy.contactPhoneNumber ?: ""
                        )
                    )
                ),
                hhVacancyLink = vacancy.hhVacancyLink
            )
        )
    }
}
