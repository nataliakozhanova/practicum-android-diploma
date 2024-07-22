package ru.practicum.android.diploma.filters.choosearea.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.domain.ErrorType
import ru.practicum.android.diploma.common.domain.Success
import ru.practicum.android.diploma.filters.choosearea.domain.api.ChooseAreaInteractor
import ru.practicum.android.diploma.filters.choosearea.domain.models.AreaInfo
import ru.practicum.android.diploma.filters.choosearea.domain.models.AreasResult
import ru.practicum.android.diploma.filters.choosearea.presentation.models.AreasByParentIdState
import ru.practicum.android.diploma.filters.choosearea.presentation.models.AreasWithCountriesState

class ChooseRegionViewModel(
    private val chooseAreaInteractor: ChooseAreaInteractor,
) : ViewModel() {

    private val _stateAreaById = MutableLiveData<AreasByParentIdState>()
    fun observeAreasBiIdState(): LiveData<AreasByParentIdState> = _stateAreaById

    private val _stateAreaWithCountry = MutableLiveData<AreasWithCountriesState>()
    fun observeAreasWithCountriesState(): LiveData<AreasWithCountriesState> = _stateAreaWithCountry

    private var allAreas: List<AreaInfo> = emptyList()

    // запрос списка регионов, когда страна не выбрана - страна для вывода в визуалку подтягивается из результатов запроса

    fun chooseOnlyArea() {
        renderAreasWithCountriesState(AreasWithCountriesState.Loading)
        viewModelScope.launch {
            chooseAreaInteractor.getAreasWithCountries().collect { pair ->
                processOnlyAreaResult(pair.first, pair.second)
            }
        }
    }

    private fun processOnlyAreaResult(areas: AreasResult?, errorType: ErrorType) {
        when (errorType) {
            is Success -> {
                if (areas != null) {
                    allAreas = areas.areas
                    renderAreasWithCountriesState(
                        AreasWithCountriesState.Content(areas.areas)
                    )
                } else {
                    renderAreasWithCountriesState(
                        AreasWithCountriesState.Empty
                    )
                }
            }

            else -> {
                renderAreasWithCountriesState(
                    AreasWithCountriesState.Error(errorType)
                )
            }
        }
    }

    private fun renderAreasWithCountriesState(state: AreasWithCountriesState) {
        _stateAreaWithCountry.postValue(state)
    }

    // запрос региона, когда страна уже выбрана ранее

    fun chooseAreasByParentId(countryId: String) {
        renderAreasByParentId(AreasByParentIdState.Loading)
        viewModelScope.launch {
            chooseAreaInteractor.getAreaByParentId(countryId).collect { pair ->
                processAreasByParentIdResult(pair.first, pair.second)
            }
        }
    }

    private fun processAreasByParentIdResult(areas: AreasResult?, errorType: ErrorType) {
        when (errorType) {
            is Success -> {
                if (areas != null) {
                    allAreas = areas.areas
                    renderAreasByParentId(
                        AreasByParentIdState.Content(areas.areas)
                    )
                } else {
                    renderAreasByParentId(
                        AreasByParentIdState.Empty
                    )
                }
            }

            else -> {
                renderAreasByParentId(
                    AreasByParentIdState.Error(errorType)
                )
            }
        }
    }

    private fun renderAreasByParentId(state: AreasByParentIdState) {
        _stateAreaById.postValue(state)
    }

    fun saveAreaWithCountrySettings(area: AreaInfo) {
        chooseAreaInteractor.saveAreaSettings(AreaInfo(id = area.id, name = area.name, countryInfo = area.countryInfo))
    }

    // метод для поиска областей
    fun searchAreas(query: String) {
        val filteredAreas = if (query.isEmpty()) {
            allAreas
        } else {
            allAreas.filter { it.name.contains(query, ignoreCase = true) }
        }
        if (filteredAreas.isEmpty()) {
            _stateAreaById.postValue(AreasByParentIdState.Empty)
        } else {
            _stateAreaById.postValue(AreasByParentIdState.Content(filteredAreas))
        }
    }
}
