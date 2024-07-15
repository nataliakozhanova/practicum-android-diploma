package ru.practicum.android.diploma.vacancydetails.domain.api

import android.content.Intent

interface ExternalNavigator {
    fun shareVacancy(link: String): Intent
}
