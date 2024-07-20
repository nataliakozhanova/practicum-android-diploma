package ru.practicum.android.diploma.filters.choosearea.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.data.ErrorType
import ru.practicum.android.diploma.common.data.Success
import ru.practicum.android.diploma.filters.choosearea.domain.api.ChooseAreaInteractor
import ru.practicum.android.diploma.filters.choosearea.domain.models.AreaInfo
import ru.practicum.android.diploma.filters.choosearea.domain.models.CountriesResult
import ru.practicum.android.diploma.filters.choosearea.domain.models.CountryInfo
import ru.practicum.android.diploma.filters.choosearea.presentation.models.CountriesState

class ChooseCountryViewModel(
    private val chooseAreaInteractor: ChooseAreaInteractor,
) : ViewModel() {

    private val _stateCountries = MutableLiveData<CountriesState>()
    fun observeCountriesState(): LiveData<CountriesState> = _stateCountries

    fun chooseCountry() {
        renderCountriesState(CountriesState.Loading)
        viewModelScope.launch {
            chooseAreaInteractor.getCountries().collect { pair ->
                processCountriesResult(pair.first, pair.second)
            }
        }
    }

    private fun processCountriesResult(countries: CountriesResult?, errorType: ErrorType) {
        when (errorType) {
            is Success -> {
                if (countries != null) {
                    renderCountriesState(
                        CountriesState.Content(countries.countries)
                    )
                } else {
                    renderCountriesState(
                        CountriesState.Empty
                    )
                }
            }

            else -> {
                renderCountriesState(
                    CountriesState.Error(errorType)
                )
            }
        }
    }

    private fun renderCountriesState(state: CountriesState) {
        _stateCountries.postValue(state)
    }

    fun saveCountrySettings(country: CountryInfo) {
        chooseAreaInteractor.saveAreaSettings(AreaInfo(id = "", name = "", countryInfo = country))
    }

}
