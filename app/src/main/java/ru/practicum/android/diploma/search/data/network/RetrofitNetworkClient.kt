package ru.practicum.android.diploma.search.data.network

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.common.data.BadRequestError
import ru.practicum.android.diploma.common.data.NetworkClient
import ru.practicum.android.diploma.common.data.NoInternetError
import ru.practicum.android.diploma.common.data.ResponseBase
import ru.practicum.android.diploma.common.data.ServerInternalError
import ru.practicum.android.diploma.search.data.HhQueryOptions
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.search.domain.models.VacanciesNotFoundType
import ru.practicum.android.diploma.search.domain.models.VacancySearchRequest
import ru.practicum.android.diploma.util.isConnected
import java.io.IOException
import java.net.HttpURLConnection

class RetrofitNetworkClient(
    private val apiService: HhApiService,
    private val context: Context,
) : NetworkClient {

    override suspend fun doRequest(dto: Any): ResponseBase {
        if (!isConnected(context)) {
            return ResponseBase(NoInternetError())
        }
        return withContext(Dispatchers.IO) {
            try {
                when (dto) {
                    is VacancySearchRequest -> {
                        val options = convertToRequestOptions(dto)

                        val response = apiService.findVacancies(options)
                        when (response.code()) {
                            HttpURLConnection.HTTP_OK -> convertVacancySearchResponse(response.body())
                            HttpURLConnection.HTTP_NOT_FOUND -> ResponseBase(VacanciesNotFoundType())
                            else -> ResponseBase(BadRequestError())
                        }

                    }

                    else -> ResponseBase(BadRequestError())
                }

            } catch (e: HttpException) {
                e.message?.let { Log.e("Http", it) }
                ResponseBase(ServerInternalError())
            }
            // выполняется, если в эмуляторе интернет смартфона включен, а на компьютере интернет отпал
            catch (e: IOException) {
                e.message?.let { Log.e("IO", it) }
                ResponseBase(ServerInternalError())
            }
        }
    }

    // зададим поля для запроса к API
    private fun convertToRequestOptions(searchRequest: VacancySearchRequest): HashMap<String, String> {
        val options: HashMap<String, String> = HashMap()
        // маска поиска
        options[HhQueryOptions.TEXT.key] = searchRequest.expression
        // по какому полю искать
        options[HhQueryOptions.SEARCH_FIELD.key] = HhQueryOptions.SEARCH_FIELD.value
        // сортировка результатов
        options[HhQueryOptions.VACANCY_SEARCH_ORDER.key] = HhQueryOptions.VACANCY_SEARCH_ORDER.value
        // страница
        if (searchRequest.page != null) {
            options[HhQueryOptions.PAGE.key] = searchRequest.page.toString()
        }
        // кол-во элементов на странице
        if (searchRequest.perPage != null) {
            options[HhQueryOptions.PER_PAGE.key] = searchRequest.perPage.toString()
        }
        // фильтр по зарплате
        if (searchRequest.filters.salary != null) {
            options[HhQueryOptions.SALARY.key] = "${searchRequest.filters.salary}"
        }
        // флаг только если зп указана
        if (searchRequest.filters.onlyWithSalary) {
            options[HhQueryOptions.ONLY_WITH_SALARY.key] = "${searchRequest.filters.onlyWithSalary}"
        }
        // фильтр по региону
        if (!searchRequest.filters.areaId.isNullOrEmpty()) {
            options[HhQueryOptions.AREA.key] = "${searchRequest.filters.areaId}"
        }
        // фильтр по индустрии
        if (!searchRequest.filters.industryId.isNullOrEmpty()) {
            options[HhQueryOptions.INDUSTRY.key] = "${searchRequest.filters.industryId}"
        }

        Log.d("mine", "Options = $options")

        return options
    }

    private fun convertVacancySearchResponse(responseBody: VacancySearchResponse?): ResponseBase =
        if (responseBody == null || responseBody.items.isEmpty()) {
            ResponseBase(VacanciesNotFoundType())
        } else {
            VacancySearchResponse(
                responseBody.found,
                responseBody.page,
                responseBody.pages,
                responseBody.perPage,
                responseBody.items,
            )
        }

}
