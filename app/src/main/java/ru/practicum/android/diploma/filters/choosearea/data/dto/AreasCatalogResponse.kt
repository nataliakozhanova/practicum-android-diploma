package ru.practicum.android.diploma.filters.choosearea.data.dto

import ru.practicum.android.diploma.common.data.ResponseBase

data class AreasCatalogResponse(
    val areasCatalog: List<AreasCatalogDto>
) : ResponseBase()
