package ru.practicum.android.diploma.filters.chooseindustry.ui

import android.view.View
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
        val clickHandler = View.OnClickListener {
            clickListener.onClick(item)
            binding.radioButton.isChecked = true // установить анимацию выбора
        }
        itemView.setOnClickListener(clickHandler)
        binding.radioButton.setOnClickListener(clickHandler)
    }
}
