package ru.practicum.android.diploma.favorites.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.domain.VacancyBase
import ru.practicum.android.diploma.favorites.data.converters.VacancyDbConverter
import ru.practicum.android.diploma.favorites.data.db.VacancyDatabase
import ru.practicum.android.diploma.favorites.data.db.VacancyEntity
import ru.practicum.android.diploma.favorites.domain.db.FavouriteVacancyRepository

class FavouriteVacancyRepositoryImpl(
    private val vacancyDatabase: VacancyDatabase,
    private val vacancyDbConverter: VacancyDbConverter
) : FavouriteVacancyRepository {
    override suspend fun addVacancyToFavourite(vacancyId: VacancyBase) {
        val vacancyEntity = convertToVacancyEntity(vacancyId)
        vacancyDatabase.vacancyDao().addVacancyToFavourite(vacancyEntity)
    }

    override suspend fun deleteVacancyFromFavourite(vacancyId: VacancyBase) {
        val vacancyEntity = convertToVacancyEntity(vacancyId)
        vacancyDatabase.vacancyDao().deleteVacancyFromFavourite(vacancyEntity)
    }

    override fun getAllFavouritesVacancies(): Flow<ArrayList<VacancyBase>> {
        val list =
            vacancyDatabase.vacancyDao().getAllFavouritesVacancies().map { vacancy -> convertToVacancyBase(vacancy) }
        return list
    }

    override fun getAllFavouritesVacanciesId(): Flow<List<String>> = flow {
        val vacancyId = vacancyDatabase.vacancyDao().getAllFavouritesVacanciesId()
        emit(vacancyId)
    }


    private fun convertToVacancyEntity(track: VacancyBase): VacancyEntity {
        return vacancyDbConverter.map(track)
    }

    private fun convertToVacancyBase(vacancies: List<VacancyEntity>): ArrayList<VacancyBase> {
        return ArrayList(vacancies.map { vacancy -> vacancyDbConverter.map(vacancy) })
    }
}
