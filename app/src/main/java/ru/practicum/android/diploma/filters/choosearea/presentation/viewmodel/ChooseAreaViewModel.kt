package ru.practicum.android.diploma.filters.choosearea.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.data.ErrorType
import ru.practicum.android.diploma.common.data.Success
import ru.practicum.android.diploma.filters.choosearea.domain.api.ChooseAreaInteractor
import ru.practicum.android.diploma.filters.choosearea.domain.models.CountriesResult
import ru.practicum.android.diploma.filters.choosearea.presentation.models.AreasByParentIdState
import ru.practicum.android.diploma.filters.choosearea.presentation.models.AreasWithCountryState
import ru.practicum.android.diploma.filters.choosearea.presentation.models.CountriesState

class ChooseAreaViewModel(
    private val chooseAreaInteractor: ChooseAreaInteractor,
) : ViewModel() {

    private val _stateCountries = MutableLiveData<CountriesState>()
    fun observeCountriesState(): LiveData<CountriesState> = _stateCountries

    private val _stateAreaById = MutableLiveData<AreasByParentIdState>()
    fun observeAreaBiIdState(): LiveData<AreasByParentIdState> = _stateAreaById

    private val _stateAreaWithCountry = MutableLiveData<AreasWithCountryState>()
    fun observeAreaWithCountryState(): LiveData<AreasWithCountryState> = _stateAreaWithCountry

    fun chooseCountry() {
        renderCountriesState(CountriesState.Loading)
        viewModelScope.launch {
            chooseAreaInteractor.getCountries().collect { pair ->
                processCountryResult(pair.first, pair.second)
            }
        }
    }

    private fun processCountryResult(countries: CountriesResult?, errorType: ErrorType) {
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

    fun chooseOnlyAreas() {
        renderOnlyAreasState(CountriesState.Loading)
        viewModelScope.launch {
            chooseAreaInteractor.getCountries().collect { pair ->
                processOnlyAreasResult(pair.first, pair.second)
            }
        }
    }

    private fun processOnlyAreasResult(countries: CountriesResult?, errorType: ErrorType) {
        when (errorType) {
            is Success -> {
                if (countries != null) {
                    renderOnlyAreasState(
                        CountriesState.Content(countries.countries)
                    )
                } else {
                    renderOnlyAreasState(
                        CountriesState.Empty
                    )
                }
            }

            else -> {
                renderOnlyAreasState(
                    CountriesState.Error(errorType)
                )
            }
        }
    }

    private fun renderOnlyAreasState(state: CountriesState) {
        _stateCountries.postValue(state)
    }
}
