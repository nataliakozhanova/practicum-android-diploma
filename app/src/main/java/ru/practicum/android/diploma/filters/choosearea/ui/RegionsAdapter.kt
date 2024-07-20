package ru.practicum.android.diploma.filters.choosearea.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemChoosingRegionCountryBinding
import ru.practicum.android.diploma.filters.choosearea.domain.models.AreaInfo

class RegionsAdapter(
    private val regionsClickListener: RegionsClickListener,
) : RecyclerView.Adapter<RegionsViewHolder>() {

    var areas: ArrayList<AreaInfo> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionsViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return RegionsViewHolder(
            regionsClickListener,
            ItemChoosingRegionCountryBinding.inflate(layoutInspector, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RegionsViewHolder, position: Int) {
        holder.bind(areas[position])
    }

    override fun getItemCount(): Int {
        return areas.size
    }

    fun interface RegionsClickListener {
        fun onRegionClick(item: AreaInfo)
    }

}
