package ru.practicum.android.diploma.vacancydetails.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.common.data.ResponseBase

data class DetailsDto(
    val id: String,
    val name: String,
    val area: AreaDto,
    val address: AddressDto?,
    val employer: EmployerDto,
    val salary: SalaryDto?,
    val experience: ExperienceDto,
    val working: WorkingDto,
    val keySkill: KeySkillDto,
    val contacts: ContactsDto,
    @SerializedName("show_logo_in_search")
    val showLogoInSearch: Boolean?
) : ResponseBase()

data class AreaDto(
    val id: String,
    val name: String,
    val url: String
)

data class AddressDto(
    val city: String?
)

data class ContactsDto(
    val email: String?,
    val name: String?,
    val phone: List<PhoneDto>?
)

data class PhoneDto(
    val city: String,
    val comment: String?,
    val country: String,
    val formatted: String,
    val number: String
)

data class EmployerDto(
    val id: String?,
    val name: String,
    @SerializedName("logo_urls")
    val logoUrls: LogoUrls?,
    val url: String?,
)

data class LogoUrls(
    @SerializedName("90")
    val logo90: String?,
    @SerializedName("240")
    val logo240: String?,
    val original: String?,
)

data class SalaryDto(
    val currency: String?,
    val from: Int?,
    val to: Int?,
    val gross: Boolean?, // Признак что границы зарплаты указаны до вычета налогов
)

data class ExperienceDto(
    val id: String,
    val name: String
)

data class KeySkillDto(
    val name: String
)

data class WorkingDto(
    val id: String?,
    val name: String
)
