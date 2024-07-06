package ru.practicum.android.diploma.search.presentation.models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.util.SingleLiveEvent
import ru.practicum.android.diploma.vacancydetail.presentation.models.Vacancy

class SearchViewModel(
    private val context: Context,
    private val interactor: VacanciesInteractor,
) : ViewModel() {
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var latestSearchText: String? = null
    private var searchJob: Job? = null

    private val _toast = SingleLiveEvent<String>()
    fun observeToast(): LiveData<String> = _toast

    private val _state = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = _state

    fun searchDebounce(changedText: String, force: Boolean) {
        if (changedText.isEmpty() || (latestSearchText == changedText && !force)) {
            return
        }
        latestSearchText = changedText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(SearchState.Loading)
            viewModelScope.launch {
                interactor.findVacancies(newSearchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(found: List<Vacancy>?, errorMessage: String?) {
        val vacancies = mutableListOf<Vacancy>()
        if (found != null) {
            vacancies.addAll(found)
        }
        when (errorMessage) {
            null -> {
                renderState(
                    SearchState.Content(vacancies)
                )
            }

            context.getString(R.string.no_vacancies) -> {
                renderState(
                    SearchState.Empty(errorMessage)
                )
            }

            else -> {
                renderState(
                    SearchState.Error(errorMessage)
                )
            }

        }
    }

    private fun renderState(state: SearchState) {
        _state.postValue(state)
    }
}
