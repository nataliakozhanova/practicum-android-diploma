package ru.practicum.android.diploma.filters.choosearea.data.network

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
import ru.practicum.android.diploma.filters.choosearea.data.dto.AreasCatalogDto
import ru.practicum.android.diploma.filters.choosearea.data.dto.AreasCatalogRequest
import ru.practicum.android.diploma.filters.choosearea.data.dto.AreasCatalogResponse
import ru.practicum.android.diploma.filters.choosearea.data.dto.AreasRequest
import ru.practicum.android.diploma.filters.choosearea.data.dto.CountriesRequest
import ru.practicum.android.diploma.filters.choosearea.data.dto.CountriesResponce
import ru.practicum.android.diploma.filters.choosearea.domain.models.AreasNotFoundType
import ru.practicum.android.diploma.filters.choosearea.domain.models.CountryInfo
import ru.practicum.android.diploma.util.isConnected
import java.io.IOException
import java.net.HttpURLConnection

class AreasRetrofitNetworkClient(
    private val hhApiServiceAreas: HhApiServiceAreas,
    private val context: Context,
) : NetworkClient {
    override suspend fun doRequest(dto: Any): ResponseBase {
        if (!isConnected(context)) {
            return ResponseBase(NoInternetError())
        }
        return withContext(Dispatchers.IO) {
            try {
                when (dto) {
                    is CountriesRequest -> requestCountries()

                    is AreasCatalogRequest -> {
                        val response = hhApiServiceAreas.getAreasByParentId(dto.areaId)
                        when (response.code()) {
                            HttpURLConnection.HTTP_OK -> convertAreasCatalogDto(response.body())
                            HttpURLConnection.HTTP_NOT_FOUND -> ResponseBase(AreasNotFoundType())
                            else -> ResponseBase(BadRequestError())
                        }
                    }

                    is AreasRequest -> {
                        val response = hhApiServiceAreas.getAreas()
                        when (response.code()) {
                            HttpURLConnection.HTTP_OK -> convertAreasCatalogResponse(response.body())
                            HttpURLConnection.HTTP_BAD_REQUEST -> ResponseBase(AreasNotFoundType())
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

    private suspend fun requestCountries(): ResponseBase {
        val response = hhApiServiceAreas.getCountries()
        return when (response.code()) {
            HttpURLConnection.HTTP_OK -> CountriesResponce(
                response.body()?.toList()?.map {
                    CountryInfo(
                        name = it.name,
                        id = it.id
                    )
                }
            )

            HttpURLConnection.HTTP_NOT_FOUND -> ResponseBase(AreasNotFoundType())
            else -> ResponseBase(BadRequestError())
        }
    }

    private fun convertAreasCatalogResponse(responseBody: AreasCatalogResponse?): ResponseBase =
        if (responseBody == null) {
            ResponseBase(AreasNotFoundType())
        } else {
            AreasCatalogResponse(
                responseBody.areasCatalog
            )
        }

    private fun convertAreasCatalogDto(responseBody: AreasCatalogDto?): ResponseBase =
        responseBody ?: ResponseBase(AreasNotFoundType())

}
