package ru.practicum.android.diploma.filters.settingsfilters.ui

import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.presentation.FilterArrow

class SettingsFiltersBinding(
    val fragment: SettingsFiltersFragment,
) {

    fun onClickFilterArrows() {
        with(fragment.binding) {
            filterArrowForward1.setOnClickListener {
                if (filterArrowForward1.tag == FilterArrow.FORWARD.drawableId) {
                    openAreaSettings()
                } else {
                    fragment.clearAreaSettings()
                }
            }
            filterArrowForward2.setOnClickListener {
                if (filterArrowForward2.tag == FilterArrow.FORWARD.drawableId) {
                    openIndustrySettings()
                } else {
                    fragment.clearIndustrySettings()
                }
            }
        }
    }

    fun openAreaSettings() {
        fragment.findNavController().navigate(
            R.id.action_filterFragment_to_chooseAreaFragment,
        )
        fragment.saveCurrentAreaFilters()
    }

    fun openIndustrySettings() {
        fragment.findNavController().navigate(
            R.id.action_filterFragment_to_chooseIndustryFragment,
        )
    }
}
