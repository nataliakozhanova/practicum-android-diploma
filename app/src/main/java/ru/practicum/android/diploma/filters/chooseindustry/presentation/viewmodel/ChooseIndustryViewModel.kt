package ru.practicum.android.diploma.filters.chooseindustry.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.data.ErrorType
import ru.practicum.android.diploma.common.data.Success
import ru.practicum.android.diploma.filters.chooseindustry.domain.interfaces.IndustryInteractor
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustriesModel
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustriesResult
import ru.practicum.android.diploma.filters.chooseindustry.presentation.models.IndustriesStates

class ChooseIndustryViewModel(private val interactor: IndustryInteractor) : ViewModel() {
    private val _stateIndustry: MutableLiveData<IndustriesStates> = MutableLiveData()
    fun observeIndustryState(): LiveData<IndustriesStates> = _stateIndustry

    private var selectedIndustry: IndustriesModel? = null

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

    fun selectIndustry(industry: IndustriesModel) {
        selectedIndustry = if (selectedIndustry == industry) null else industry
    }

    fun saveSelectedIndustry() {
        selectedIndustry?.let {
            interactor.saveIndustrySettings(it)
        }
    }
}
