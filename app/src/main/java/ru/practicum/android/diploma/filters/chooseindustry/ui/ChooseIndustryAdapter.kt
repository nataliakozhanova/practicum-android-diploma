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

    var industries: List<IndustriesModel> = emptyList()

    fun setItems(items: List<IndustriesModel>) {
        industries = items
        notifyDataSetChanged()
    }
    private var selectedIndustry: IndustriesModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseIndustryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ChooseIndustryViewHolder(
            industriesClickListener,
            ItemChoosingIndustryBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return industries.size
    }

    override fun onBindViewHolder(holder: ChooseIndustryViewHolder, position: Int) {
        val industry = industries[position]
        holder.bind(industry, industry == selectedIndustry)
    }

    fun selectIndustry(industry: IndustriesModel) {
        val previousSelected = selectedIndustry
        selectedIndustry = if (selectedIndustry == industry) null else industry
        // Обновляем только измененные позиции
        previousSelected?.let { notifyItemChanged(industries.indexOf(it)) }
        notifyItemChanged(industries.indexOf(industry))
    }
}
