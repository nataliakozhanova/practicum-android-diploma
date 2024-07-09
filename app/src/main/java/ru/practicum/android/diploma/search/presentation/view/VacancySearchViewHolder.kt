package ru.practicum.android.diploma.search.presentation.view

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.domain.SalaryInfo
import ru.practicum.android.diploma.common.domain.VacancyBase
import ru.practicum.android.diploma.databinding.ItemVacancyViewBinding
import ru.practicum.android.diploma.search.presentation.models.Currency

class VacancySearchViewHolder(
    private val clickListener: VacancySearchAdapter.SearchClickListener,
    private val binding: ItemVacancyViewBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private val trackCornerRadius: Int =
        itemView.context.resources.getDimensionPixelSize(R.dimen.logo_corner_radius)

    private val stringBuilderNameAndCity = StringBuilder()
    private fun moneyFormat(num: Int) = "%,d".format(num).replace(",", " ")

    inline fun <reified T : Enum<T>> enumValueOfOrNull(name: String): T? {
        return enumValues<T>().find { it.name == name }
    }

    private fun currencyFromStr(name: String?): String {
        return enumValueOfOrNull<Currency>(name.toString())?.abbr ?: name.toString()
    }

    private fun formatSalary(salaryInfo: SalaryInfo?): String {
        val salaryFrom = salaryInfo?.salaryFrom ?: 0
        val salaryTo = salaryInfo?.salaryTo ?: 0
        with(itemView.context) {
            val currency = currencyFromStr(salaryInfo?.salaryCurrency)
            return when {
                salaryFrom > 0 && salaryTo > 0 -> getString(
                    R.string.salary_from_to,
                    moneyFormat(salaryFrom),
                    moneyFormat(salaryTo),
                    currency
                )

                salaryFrom > 0 && salaryTo == 0 -> getString(R.string.salary_from, moneyFormat(salaryFrom), currency)
                salaryFrom == 0 && salaryTo > 0 -> getString(R.string.salary_to, moneyFormat(salaryTo), currency)
                else -> getString(R.string.salary_not_set)
            }
        }
    }

    fun bind(item: VacancyBase) {
        binding.vacancyNameAndCity.text = stringBuilderNameAndCity
            .append(item.name + ", " + item.employerInfo.areaName)
        binding.companyName.text = item.employerInfo.employerName
        binding.salaryText.text = formatSalary(item.salaryInfo)
        Glide.with(itemView)
            .load(item.employerInfo.employerLogoUrl)
            .placeholder(R.drawable.logo_placeholder_image)
            .transform(CenterCrop(), RoundedCorners(trackCornerRadius))
            .into(binding.vacancyLogo)

        itemView.setOnClickListener { clickListener.onVacancyClick(item) }
    }
}
