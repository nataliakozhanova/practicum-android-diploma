package ru.practicum.android.diploma.sharingandcontactvacancy.data

import android.content.Intent
import android.net.Uri
import ru.practicum.android.diploma.sharingandcontactvacancy.domain.api.ExternalNavigator

class ExternalNavigatorImpl() : ExternalNavigator {
    override fun shareVacancy(link: String): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(Intent.EXTRA_TEXT, link)
        return (Intent.createChooser(shareIntent, ""))
    }

    override fun contactVacancyByPhone(phone: String): Intent {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phone")
        return intent
    }

    override fun contactVacancyByEmail(email: String): Intent {
        val writeIntent = Intent(Intent.ACTION_SENDTO)
        writeIntent.data = Uri.parse("mailto:$email")
        return writeIntent
    }
}
