package ru.practicum.android.diploma.filters.choosearea.data.dto

import ru.practicum.android.diploma.common.data.ResponseBase
import ru.practicum.android.diploma.filters.choosearea.domain.models.CountryInfo

class CountriesResponce(val countries: List<CountryInfo>?) : ResponseBase()

data class CountryDto(
    val id: String,
    val name: String
)
