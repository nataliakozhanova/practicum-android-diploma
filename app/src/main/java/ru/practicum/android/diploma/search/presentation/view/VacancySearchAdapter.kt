package ru.practicum.android.diploma.search.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemVacancyViewBinding
import ru.practicum.android.diploma.vacancydetails.presentation.models.Vacancy

class VacancySearchAdapter(
    private val searchClickListener: SearchClickListener,
) : RecyclerView.Adapter<VacancySearchViewHolder>() {

    var vacancies: ArrayList<Vacancy> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancySearchViewHolder {

        val layoutInspector = LayoutInflater.from(parent.context)
        return VacancySearchViewHolder(
            searchClickListener,
            ItemVacancyViewBinding.inflate(layoutInspector, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VacancySearchViewHolder, position: Int) {
        holder.bind(vacancies[position])
    }

    override fun getItemCount(): Int {
        return vacancies.size
    }

    fun interface SearchClickListener {
        fun onVacancyClick(vacancy: Vacancy)
    }

}
