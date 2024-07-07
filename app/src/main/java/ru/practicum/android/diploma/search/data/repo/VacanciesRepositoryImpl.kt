package ru.practicum.android.diploma.search.data.repo

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.data.NetworkClient
import ru.practicum.android.diploma.search.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.search.domain.api.VacanciesRepository
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancydetails.presentation.models.Vacancy
import java.net.HttpURLConnection

class VacanciesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val context: Context,
) : VacanciesRepository {
    override fun findVacancies(expression: String, page: Int?, perPage: Int?): Flow<Resource<List<Vacancy>>> = flow {
        val response = networkClient.doRequest(VacancySearchRequest(expression, page, perPage))
        when (response.resultCode) {
            HttpURLConnection.HTTP_OK -> {
                if ((response as VacancySearchResponse).items.isEmpty()) {
                    emit(Resource.Error(context.getString(R.string.no_vacancies)))
                } else {
                    emit(Resource.Success(response.items.map {
                        Vacancy(
                            id = it.id,
                            name = it.name,
                        )
                    }))
                }
            }

            else -> {
                emit(Resource.Error(context.getString(R.string.server_error)))
            }
        }
    }
}
