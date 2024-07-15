package ru.practicum.android.diploma.vacancydetails.data.repo

import android.content.Intent
import ru.practicum.android.diploma.vacancydetails.domain.api.ExternalNavigator

class ExternalNavigatorImpl : ExternalNavigator {
    override fun shareVacancy(link: String): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(Intent.EXTRA_TEXT, link)
        return Intent.createChooser(shareIntent, "")
    }
}
