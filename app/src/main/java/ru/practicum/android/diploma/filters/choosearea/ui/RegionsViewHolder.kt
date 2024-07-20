package ru.practicum.android.diploma.filters.choosearea.ui

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemChoosingRegionCountryBinding
import ru.practicum.android.diploma.filters.choosearea.domain.models.AreaInfo

class RegionsViewHolder(
    private val clickListener: RegionsAdapter.RegionsClickListener,
    private val binding: ItemChoosingRegionCountryBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AreaInfo) {
        binding.name.text = item.name
        itemView.setOnClickListener { clickListener.onRegionClick(item) }
    }
}
