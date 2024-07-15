package ru.practicum.android.diploma.sharingandcontactvacancy.domain.api

import android.content.Intent

interface SharingContactInteractor {
    fun shareVacancy(): Intent
    fun contactVacancyByPhone(): Intent
    fun contactVacancyByEmail(): Intent
}
