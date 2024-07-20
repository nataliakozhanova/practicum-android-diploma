package ru.practicum.android.diploma.filters.chooseindustry.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemChoosingIndustryBinding
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustriesModel

class ChooseIndustryAdapter(private val industriesClickListener: IndustryClickListener) :
    RecyclerView.Adapter<ChooseIndustryViewHolder>() {
    fun interface IndustryClickListener {
        fun onClick(item: IndustriesModel)
    }

    var industries: ArrayList<IndustriesModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseIndustryViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return ChooseIndustryViewHolder(
            industriesClickListener,
            ItemChoosingIndustryBinding.inflate(layoutInspector, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return industries.size
    }

    override fun onBindViewHolder(holder: ChooseIndustryViewHolder, position: Int) {
        holder.bind(industries[position])
    }
}
