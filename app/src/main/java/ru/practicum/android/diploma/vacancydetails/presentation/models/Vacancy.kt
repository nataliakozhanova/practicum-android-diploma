package ru.practicum.android.diploma.vacancydetails.presentation.models

import java.util.Currency

data class Vacancy(
    val id: String,
    val name: String,
    val region: String,
    val city: String?,
    val companyId: String?,
    val companyName: String?,
    val companyLogo: String?,
    val salaryCurrency: String?,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val employment: String?,// тип занятости
    val experience: String?,// требуемый опыт
) {
    // заглушка, дописать функцию
    fun Salary() = "$salaryFrom - $salaryTo $salaryCurrency"
}
