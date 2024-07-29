package ru.practicum.android.diploma.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.common.domain.VacancyBase
import ru.practicum.android.diploma.databinding.ItemVacancyViewBinding

class VacancyAdapter(
    private val vacancyClickListener: VacancyClickListener,
) : RecyclerView.Adapter<VacancyViewHolder>() {

    var vacancies: ArrayList<VacancyBase> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return VacancyViewHolder(
            vacancyClickListener,
            ItemVacancyViewBinding.inflate(layoutInspector, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(vacancies[position])
    }

    override fun getItemCount(): Int {
        return vacancies.size
    }

    fun interface VacancyClickListener {
        fun onVacancyClick(vacancy: VacancyBase)
    }

}
