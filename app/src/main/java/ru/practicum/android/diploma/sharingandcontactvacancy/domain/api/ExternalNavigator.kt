package ru.practicum.android.diploma.sharingandcontactvacancy.domain.api

import android.content.Intent

interface ExternalNavigator {
    fun shareVacancy(link :String): Intent
    fun contactVacancyByPhone(phone: String): Intent
    fun contactVacancyByEmail(email : String): Intent
}
