package ru.practicum.android.diploma.search.data.api

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.data.NetworkClient
import ru.practicum.android.diploma.search.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.search.domain.api.VacanciesRepository
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancy_details.presentation.models.Vacancy
import java.net.HttpURLConnection

class VacanciesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val context: Context,
) : VacanciesRepository {
    override fun findVacancies(expression: String): Flow<Resource<List<Vacancy>>> = flow {
        val response = networkClient.doRequest(VacancySearchRequest(expression))
        when (response.resultCode) {
            HttpURLConnection.HTTP_OK -> {
                Log.d("mine", (response as VacancySearchResponse).toString())
                emit(Resource.Error(context.getString(R.string.no_vacancies)))
                /*if ((response as VacancySearchResponse).items.isEmpty()) {
                    emit(Resource.Error(context.getString(R.string.no_vacancies)))
                } else {
                    emit(Resource.Success(response.items.map {
                        Vacancy(
                            id = it.id,
                            name = it.name,
                        )
                    }))
                }*/
            }

            else -> {
                emit(Resource.Error(context.getString(R.string.server_error)))
            }
        }
    }
}
