package ru.practicum.android.diploma.filters.chooseindustry.ui

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemChoosingIndustryBinding
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustriesModel

class ChooseIndustryViewHolder(
    private val clickListener: ChooseIndustryAdapter.IndustryClickListener,
    private val binding: ItemChoosingIndustryBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: IndustriesModel, isSelected: Boolean) {
        binding.industryName.text = item.name
        itemView.setOnClickListener { clickListener.onClick(item) }
        binding.radioButton.isChecked = isSelected
    }
}
