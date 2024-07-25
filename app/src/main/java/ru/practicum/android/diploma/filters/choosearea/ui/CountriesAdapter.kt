package ru.practicum.android.diploma.filters.choosearea.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemChoosingRegionCountryBinding
import ru.practicum.android.diploma.filters.choosearea.domain.models.CountryInfo

class CountriesAdapter(
    private val countriesClickListener: CountriesClickListener,
) : RecyclerView.Adapter<CountriesViewHolder>() {

    var countries: ArrayList<CountryInfo> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return CountriesViewHolder(
            countriesClickListener,
            ItemChoosingRegionCountryBinding.inflate(layoutInspector, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        holder.bind(countries[position])
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    fun interface CountriesClickListener {
        fun onCountryClick(item: CountryInfo)
    }

}
