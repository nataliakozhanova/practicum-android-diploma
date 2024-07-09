package ru.practicum.android.diploma.favorites.presentation.view

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.*
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemFavoriteViewBinding
import ru.practicum.android.diploma.favorites.domain.models.FavouriteVacanciesModel

class FavoriteViewHolder(
    private val binding: ItemFavoriteViewBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private val trackCornerRadius: Int =
        itemView.context.resources.getDimensionPixelSize(R.dimen.logo_corner_radius)

    fun bind(item: FavouriteVacanciesModel) {
        binding.titleFavoriteItem.text = item.name
        binding.subtitleFavoriteItem.text = item.employerName
        binding.priceFavoriteItem.text = formatSalary(item.salaryFrom, item.salaryTo, item.salaryCurrency)

        with(itemView)
            .load(item.employerLogoUrl)
            .placeholder(R.drawable.logo_placeholder_image)
            .transform(CenterCrop(), RoundedCorners(trackCornerRadius))
            .into(binding.imageItemFavorite)
    }

    private fun formatSalary(min: Int?, max: Int?, currency: String): String {
        return when {
            min != null && max != null -> "от $min до $max $currency"
            min != null -> "от $min $currency"
            max != null -> "до $max $currency"
            else -> "зарплата не указана"
        }
    }
}
