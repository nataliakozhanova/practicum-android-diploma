package ru.practicum.android.diploma.search.presentation.view

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.domain.VacancyBase
import ru.practicum.android.diploma.databinding.ItemVacancyViewBinding

class VacancySearchViewHolder(
    private val clickListener: VacancySearchAdapter.SearchClickListener,
    private val binding: ItemVacancyViewBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private val trackCornerRadius: Int =
        itemView.context.resources.getDimensionPixelSize(R.dimen.logo_corner_radius)

    private val stringBuilderNameAndCity = StringBuilder()
    private val myString = itemView.context.getString(R.string.initial_salary_value)

    fun bind(item: VacancyBase) {
        binding.vacancyNameAndCity.text = stringBuilderNameAndCity
            .append(item.name + ", " + item.employerInfo.areaName)
        binding.companyName.text = item.employerInfo.employerName
        binding.salaryText.text = myString.format(item.salaryInfo?.salaryFrom)

        // дописать вывод з.п. в нужном формате числа, добавить проверку на От и ДО и Зп не указана

        Glide.with(itemView)
            .load(item.employerInfo.employerLogoUrl)
            .placeholder(R.drawable.logo_placeholder_image)
            .transform(CenterCrop(), RoundedCorners(trackCornerRadius))
            .into(binding.vacancyLogo)

        itemView.setOnClickListener { clickListener.onVacancyClick(item) }
    }
}
