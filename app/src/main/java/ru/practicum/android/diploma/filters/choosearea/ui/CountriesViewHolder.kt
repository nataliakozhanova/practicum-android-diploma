package ru.practicum.android.diploma.filters.choosearea.ui

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemChoosingRegionCountryBinding
import ru.practicum.android.diploma.filters.choosearea.domain.models.CountryInfo

class CountriesViewHolder(
    private val clickListener: CountriesAdapter.CountriesClickListener,
    private val binding: ItemChoosingRegionCountryBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CountryInfo) {
        binding.name.text = item.name
        itemView.setOnClickListener { clickListener.onCountryClick(item) }
    }
}
