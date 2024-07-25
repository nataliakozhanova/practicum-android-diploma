package ru.practicum.android.diploma.util

import android.content.Context
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.domain.SalaryInfo
import ru.practicum.android.diploma.common.presentation.Currency

object Formatter {
    private fun moneyFormat(num: Int) = "%,d".format(num).replace(",", " ")

    private fun currencyFromStr(name: String?): String {
        return Currency.valueOrNull(name.toString())?.abbr ?: name.toString()
    }

    fun formatSalary(context: Context, salaryInfo: SalaryInfo?): String {
        val salaryFrom = salaryInfo?.salaryFrom ?: 0
        val salaryTo = salaryInfo?.salaryTo ?: 0
        with(context) {
            val currency = currencyFromStr(salaryInfo?.salaryCurrency)
            return when {
                salaryFrom > 0 && salaryTo == salaryFrom -> getString(
                    R.string.salary_exact,
                    moneyFormat(salaryFrom),
                    currency
                )

                salaryFrom > 0 && salaryTo > 0 -> getString(
                    R.string.salary_from_to,
                    moneyFormat(salaryFrom),
                    moneyFormat(salaryTo),
                    currency
                )

                salaryFrom > 0 && salaryTo == 0 -> getString(
                    R.string.salary_from,
                    moneyFormat(salaryFrom),
                    currency
                )

                salaryFrom == 0 && salaryTo > 0 -> getString(
                    R.string.salary_to,
                    moneyFormat(salaryTo),
                    currency
                )

                else -> getString(R.string.salary_not_set)
            }
        }
    }
}
