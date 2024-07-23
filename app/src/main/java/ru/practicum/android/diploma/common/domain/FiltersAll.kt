package ru.practicum.android.diploma.common.domain

import ru.practicum.android.diploma.filters.choosearea.domain.models.AreaInfo
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustriesModel
import ru.practicum.android.diploma.filters.settingsfilters.domain.models.SalaryFilters

data class FiltersAll(
    val salary: SalaryFilters?,
    val area: AreaInfo?,
    val industry: IndustriesModel?
)
