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
        renderState(CountriesState.Loading)
        viewModelScope.launch {
            chooseAreaInteractor.getCountries().collect { pair ->
                processResult(pair.first, pair.second)
            }
        }
    }

    private fun processResult(countries: CountriesResult?, errorType: ErrorType) {
        when (errorType) {
            is Success -> {
                if (countries?.countries != null) {
                    renderState(
                        CountriesState.Content(countries.countries)
                    )
                } else {
                    renderState(
                        CountriesState.Empty
                    )
                }
            }

            else -> {
                renderState(
                    CountriesState.Error(errorType)
                )
            }
        }
    }

    private fun renderState(state: CountriesState) {
        _stateCountries.postValue(state)
    }
}
