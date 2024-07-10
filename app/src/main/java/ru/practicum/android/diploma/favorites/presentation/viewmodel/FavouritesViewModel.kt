package ru.practicum.android.diploma.favorites.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.favorites.domain.db.FavouriteVacancyInteractor
import ru.practicum.android.diploma.favorites.presentation.models.FavouritesStates
import java.lang.Exception

class FavouritesViewModel(private val favouriteVacancyInteractor: FavouriteVacancyInteractor) : ViewModel() {
    private val _state = MutableLiveData<FavouritesStates>()
    val state: LiveData<FavouritesStates> = _state

    fun getAllFavouriteVacancies() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    favouriteVacancyInteractor
                        .getAllFavouritesVacancies()
                        .collect() {
                            if (it.isEmpty()) {
                                _state.postValue(FavouritesStates.Empty)
                            } else {
                                _state.postValue(FavouritesStates.NotEmpty(it))
                            }
                        }
                } catch (e: Exception) {
                    _state.postValue(FavouritesStates.Error(e))
                }
            }
        }
    }
}
