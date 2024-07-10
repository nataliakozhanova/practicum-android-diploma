package ru.practicum.android.diploma.favorites.domain.db

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.VacancyBase

interface FavouriteVacancyInteractor {
    suspend fun addVacancyToFavourite(vacancyId: VacancyBase)
    suspend fun deleteVacancyFromFavourite(vacancyId: VacancyBase)
    fun getAllFavouritesVacancies(): Flow<ArrayList<VacancyBase>>
    fun getAllFavouritesVacanciesId(): Flow<List<String>>
}
