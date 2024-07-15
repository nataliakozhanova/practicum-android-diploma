package ru.practicum.android.diploma.sharingandcontactvacancy.domain.impl

import android.content.Intent
import ru.practicum.android.diploma.sharingandcontactvacancy.domain.api.ExternalNavigator
import ru.practicum.android.diploma.sharingandcontactvacancy.domain.api.SharingContactInteractor

class SharingContactInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val hhVacancyLink: String,
    private val phone: String,
    private val email: String
) : SharingContactInteractor {
    override fun shareVacancy(): Intent {
        return externalNavigator.shareVacancy(hhVacancyLink)
    }

    override fun contactVacancyByPhone(): Intent {
        return externalNavigator.contactVacancyByPhone(phone)
    }

    override fun contactVacancyByEmail(): Intent {
        return externalNavigator.contactVacancyByEmail(email)
    }
}
