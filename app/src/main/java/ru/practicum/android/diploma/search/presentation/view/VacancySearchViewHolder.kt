package ru.practicum.android.diploma.search.presentation.view

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemVacancyViewBinding
import ru.practicum.android.diploma.vacancydetails.presentation.models.Vacancy

class VacancySearchViewHolder(
    private val clickListener: VacancySearchAdapter.SearchClickListener,
    private val binding: ItemVacancyViewBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private val trackCornerRadius: Int =
        itemView.context.resources.getDimensionPixelSize(R.dimen.logo_corner_radius)

    private val stringBuilderNameAndCity = StringBuilder()
    private val myString = itemView.context.getString(R.string.expected_salary)

//    fun bind(item: VacancySearchResultItem) {
//        binding.vacancyNameAndCity.text = stringBuilderNameAndCity
//            .append(item.name + ", " + item.employerInfo.areaName)
//        binding.companyName.text = item.employerInfo.employerName
//        binding.salaryText.text = myString.format(item.salaryInfo.salaryCurrency)
//
//        // дописать вывод з.п. в нужном формате числа, добавить проверку на От и ДО и Зп не указана
//
//        Glide.with(itemView)
//            .load(item.employerInfo.employerLogoUrl)
//            .placeholder(R.drawable.logo_placeholder_image)
//            .centerCrop()
//            .transform(RoundedCorners(trackCornerRadius))
//            .into(binding.vacancyLogo)

        fun bind(item: Vacancy) {
            binding.vacancyNameAndCity.text = stringBuilderNameAndCity
                .append(item.name + ", " + item.city)
            binding.companyName.text = item.companyName
            binding.salaryText.text = myString.format(item.salaryFrom)

            Glide.with(itemView)
                .load(item.companyLogo)
                .placeholder(R.drawable.logo_placeholder_image)
                .centerCrop()
                .transform(RoundedCorners(trackCornerRadius))
                .into(binding.vacancyLogo)

        itemView.setOnClickListener { clickListener.onVacancyClick(item) }
    }
}
