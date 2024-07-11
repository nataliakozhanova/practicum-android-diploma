package ru.practicum.android.diploma.vacancydetails.presentation.view

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemVacancyDetailsViewBinding
import ru.practicum.android.diploma.util.Formatter
import ru.practicum.android.diploma.vacancydetails.domain.models.VacancyDetails

class VacancyDetailsViewHolder(private val binding: ItemVacancyDetailsViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val trackCornerRadius: Int = itemView.context.resources.getDimensionPixelSize(R.dimen.logo_corner_radius)

    fun bind(vacancy: VacancyDetails) {
        binding.nameVacancyTv.text = "${vacancy.name}, ${vacancy.employerInfo.areaName}"
        binding.nameCompanyTv.text = vacancy.employerInfo.employerName
        binding.adressCompanyTv.text = vacancy.employerInfo.areaName
        binding.experienceTv.text = vacancy.experience
        binding.formatWorkTv.text = vacancy.formatWork
        binding.vacancySalaryTv.text = Formatter.formatSalary(itemView.context, vacancy.salaryInfo)
        Glide.with(itemView)
            .load(vacancy.employerInfo.employerLogoUrl)
            .placeholder(R.drawable.logo_placeholder_image)
            .transform(CenterCrop(), RoundedCorners(trackCornerRadius))
            .into(binding.logoCompanyIv)

        binding.vacancyResponsibilitiesTv.text = vacancy.responsibilities.joinToString("\n• ", "• ")
        binding.vacancyRequirementsTv.text = vacancy.requirements.joinToString("\n• ", "• ")
        binding.vacancyConditionsTv.text = vacancy.conditions.joinToString("\n• ", "• ")
        binding.vacancyKeySkillsTv.text = vacancy.keySkills.joinToString("\n• ", "• ")
    }
}
