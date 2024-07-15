package ru.practicum.android.diploma.vacancydetails.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.common.data.ErrorType
import ru.practicum.android.diploma.common.data.Success
import ru.practicum.android.diploma.favorites.domain.db.FavouriteVacancyInteractor
import ru.practicum.android.diploma.vacancydetails.domain.api.DetailsInteractor
import ru.practicum.android.diploma.vacancydetails.domain.models.DetailsNotFoundType
import ru.practicum.android.diploma.vacancydetails.domain.models.VacancyDetails
import ru.practicum.android.diploma.vacancydetails.presentation.models.DetailsState

class DetailsViewModel(
    private val vacancyInteractor: DetailsInteractor,
    private val favouriteVacancyInteractor: FavouriteVacancyInteractor
) : ViewModel() {

    private var isFavourite: Boolean = false
    private var isFavorite = MutableLiveData<Boolean>()
    fun observeFavoriteState(): LiveData<Boolean> = isFavorite

    private val vacancyState = MutableLiveData<DetailsState>()
    fun observeVacancyState(): LiveData<DetailsState> = vacancyState
    private var favouriteTracksId: List<String>? = null
    var vacancy: VacancyDetails? = null

    fun addToFavById(vacancyId: VacancyDetails) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favouriteVacancyInteractor.addVacancyToFavourite(vacancyId)
            }
            isFavourite = true
            vacancyState.postValue(DetailsState.isFavorite(isFavourite))
        }
    }

    fun checkVacancyInDatabase(vacancyId: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val exists = withContext(Dispatchers.IO) {
                favouriteVacancyInteractor.getVacancyById(vacancyId) != null
            }
            callback(exists)
        }
    }

    fun getVacancyDatabase(vacancyId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val vacancy = favouriteVacancyInteractor.getVacancyById(vacancyId)
                withContext(Dispatchers.Main) {
                    vacancy?.let {
                        vacancyState.postValue(DetailsState.Content(it))
                    } ?: run {
                        vacancyState.postValue(DetailsState.Empty)
                    }
                }
            }
        }
    }

    fun deleteFavouriteVacancy(vacancyId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favouriteVacancyInteractor.deleteVacancyFromFavourite(vacancyId)
            }
            isFavourite = false
            vacancyState.postValue(DetailsState.isFavorite(isFavourite))
        }
    }

    fun isFavourite(vacancyId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favouriteVacancyInteractor
                    .getAllFavouritesVacanciesId()
                    .collect {
                        favouriteTracksId = it
                    }
            }
            if (favouriteTracksId!!.contains(vacancyId)) {
                isFavourite = true
                vacancyState.postValue(DetailsState.isFavorite(isFavourite))
            } else {
                isFavourite = false
                vacancyState.postValue(DetailsState.isFavorite(isFavourite))
            }
        }
    }

    fun getVacancy(vacancyId: String) {
        renderState(DetailsState.Loading)
        viewModelScope.launch {
            vacancyInteractor.getVacancyDetail(vacancyId).collect { pair ->
                processResult(pair.first, pair.second)
            }
        }
    }

    private fun processResult(vacancyDetails: VacancyDetails?, errorType: ErrorType) {
        when (errorType) {
            is Success -> {
                if (vacancyDetails != null) {
                    renderState(
                        DetailsState.Content(vacancyDetails)
                    )
                } else {
                    renderState(
                        DetailsState.Empty
                    )
                }
            }

            is DetailsNotFoundType -> {
                renderState(
                    DetailsState.Empty
                )
            }

            else -> {
                renderState(
                    DetailsState.Error(errorType)
                )
            }
        }
    }

    private fun renderState(state: DetailsState) {
        vacancyState.postValue(state)
    }

    fun getFavouriteState(): Boolean {
        return isFavourite
    }

}
