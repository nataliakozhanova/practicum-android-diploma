package ru.practicum.android.diploma.filters.chooseindustry.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.domain.ErrorType
import ru.practicum.android.diploma.common.domain.Success
import ru.practicum.android.diploma.filters.chooseindustry.domain.interfaces.IndustryInteractor
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustriesModel
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustriesResult
import ru.practicum.android.diploma.filters.chooseindustry.presentation.models.ChosenStates
import ru.practicum.android.diploma.filters.chooseindustry.presentation.models.IndustriesStates

class ChooseIndustryViewModel(private val interactor: IndustryInteractor) : ViewModel() {

    private val _stateIndustry: MutableLiveData<IndustriesStates> = MutableLiveData()
    fun observeIndustryState(): LiveData<IndustriesStates> = _stateIndustry

    private val _stateChosen: MutableLiveData<ChosenStates> = MutableLiveData()
    fun observeIndustryStateChosen(): LiveData<ChosenStates> = _stateChosen

    private var selectedIndustry: IndustriesModel? = null
    private var allIndustries: List<IndustriesModel> = listOf()

    fun getIndustry() {
        renderState(IndustriesStates.Loading)
        viewModelScope.launch {
            interactor.getIndustries().collect { pair: Pair<IndustriesResult?, ErrorType> ->
                processResult(pair.first, pair.second)
            }
        }
    }

    private fun processResult(result: IndustriesResult?, errorType: ErrorType) {
        when (errorType) {
            is Success -> {
                if (result != null) {
                    allIndustries = result.industries
                    renderState(IndustriesStates.Content(result.industries))
                } else {
                    renderState(IndustriesStates.Empty)
                }
            }

            else -> {
                renderState(IndustriesStates.Error(errorType))
            }
        }
    }

    private fun renderState(state: IndustriesStates) {
        _stateIndustry.postValue(state)
    }

    private fun renderChosen(state: ChosenStates) {
        _stateChosen.postValue(state)
    }

    fun selectIndustry(industry: IndustriesModel) {
        if (selectedIndustry == industry) {
            selectedIndustry = null
            renderChosen(ChosenStates.NotChosen)
        } else {
            selectedIndustry = industry
            renderChosen(ChosenStates.Chosen)
        }
    }

    fun saveSelectedIndustry() {
        selectedIndustry?.let {
            interactor.saveIndustrySettings(it)
        }
    }

    fun getSelectedIndustry(): IndustriesModel? = interactor.getIndustrySettings()

    fun searchIndustries(query: String) {
        val filteredIndustries = if (query.isEmpty()) {
            allIndustries
        } else {
            allIndustries.filter { it.name.contains(query, ignoreCase = true) }
        }
        renderState(IndustriesStates.Content(filteredIndustries))
    }
}
