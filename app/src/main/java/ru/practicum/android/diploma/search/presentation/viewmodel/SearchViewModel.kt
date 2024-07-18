package ru.practicum.android.diploma.search.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.data.ErrorType
import ru.practicum.android.diploma.common.data.NoInternetError
import ru.practicum.android.diploma.common.data.Success
import ru.practicum.android.diploma.common.domain.VacancyBase
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.SearchResult
import ru.practicum.android.diploma.search.domain.models.VacanciesNotFoundType
import ru.practicum.android.diploma.search.presentation.models.SearchState
import ru.practicum.android.diploma.util.SingleLiveEvent
import ru.practicum.android.diploma.util.isConnected

class SearchViewModel(
    private val context: Context,
    private val interactor: SearchInteractor,
) : ViewModel() {
    companion object {
        const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        const val ITEMS_PER_PAGE = 20
    }

    private var vacanciesList = mutableListOf<VacancyBase>()
    private var vacanciesCheck = mutableListOf<String>()
    private var pages: Int = 0
    private var page: Int = 0
    private var latestSearchText: String? = null
    private var isNextPageLoading: Boolean = false
    private var searchJob: Job? = null

    private val _toast = SingleLiveEvent<String>()
    fun observeToast(): LiveData<String> = _toast

    private val _state = MutableLiveData<SearchState>()
    private val _nextPageState = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = _state
    fun observeNextPageState(): LiveData<SearchState> = _nextPageState

    init {
        _state.value = SearchState.Default
    }

    fun initSearch() {
        page = 0
        pages = 0
        latestSearchText = null
        vacanciesList.clear()
        vacanciesCheck.clear()
        if (searchJob != null && searchJob!!.isActive) {
            searchJob?.cancel()
        }
    }

    fun clearSearch() {
        initSearch()
        renderState(SearchState.Default, _state)
    }

    fun nextPageSearch() {
        if (!isNextPageLoading && latestSearchText != null) {
            if (!isConnected(context)) {
                renderState(SearchState.Error(NoInternetError()), _nextPageState)
            } else {
                if (page + 1 < pages) {
                    page += 1
                    searchDebounce(latestSearchText.toString(), instantStart = true)
                } else {
                    renderState(SearchState.AtBottom, _nextPageState)
                }
            }
        }
    }

    private fun badSearchConditions(newSearchText: String, instantStart: Boolean): Boolean {
        return isNextPageLoading || newSearchText.trim()
            .isEmpty() || !instantStart && page == 0 && latestSearchText == newSearchText.trim()
    }

    fun searchDebounce(changedText: String, instantStart: Boolean = false) {
        if (badSearchConditions(changedText, instantStart)) {
            return
        }
        latestSearchText = changedText.trim()
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (!instantStart) {
                delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
            }
            searchRequest(changedText.trim())
        }
    }

    private fun searchRequest(newSearchText: String) {
        // отгружаем в livedata для начального поиска или следующей страницы
        val stateSwitch = if (page > 0) {
            _nextPageState
        } else {
            _state
        }
        isNextPageLoading = page > 0
        if (newSearchText.trim().isNotEmpty()) {
            // загрузка
            renderState(SearchState.Loading, stateSwitch)
            // корутина на поиск
            viewModelScope.launch {
                interactor.findVacancies(newSearchText, page, ITEMS_PER_PAGE)
                    .collect { pair ->
                        processResult(pair.first, pair.second, stateSwitch)
                    }
            }
        }
    }

    private fun vacancyHash(vacancy: VacancyBase): String {
        return "${vacancy.name},${vacancy.employerInfo.areaName}," +
            "${vacancy.employerInfo.employerName},${vacancy.salaryInfo}"
    }

    private fun saveVacancies(vacancies: List<VacancyBase>) {
        // только уникальные вакансии
        for (newVacancy in vacancies) {
            var vacHash = vacancyHash(newVacancy)
            if (!vacanciesCheck.contains(vacHash)) {
                vacanciesList.add(newVacancy)
                vacanciesCheck.add(vacHash)
            }
        }
    }

    private fun processResult(
        searchResult: SearchResult?,
        errorType: ErrorType,
        stateSwitch: MutableLiveData<SearchState>
    ) {
        pages = searchResult?.pages ?: 0
        page = searchResult?.page ?: 0
        isNextPageLoading = false
        when (errorType) {
            is Success -> {
                if (searchResult != null) {
                    saveVacancies(searchResult.vacancies)
                    renderState(
                        SearchState.Content(vacanciesList, searchResult.found, searchResult.pages, searchResult.page),
                        stateSwitch
                    )
                }
            }

            is VacanciesNotFoundType -> {
                renderState(SearchState.Empty, stateSwitch)
            }

            else -> {
                renderState(SearchState.Error(errorType), stateSwitch)
            }

        }
    }

    private fun renderState(state: SearchState, liveData: MutableLiveData<SearchState>) {
        liveData.postValue(state)
    }

    fun searchByClick(searchText: String) {
        initSearch()
        searchDebounce(searchText, instantStart = true)
    }
}
