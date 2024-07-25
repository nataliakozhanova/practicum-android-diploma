package ru.practicum.android.diploma.filters.choosearea.data.network

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.common.data.NetworkClient
import ru.practicum.android.diploma.common.data.ResponseBase
import ru.practicum.android.diploma.common.domain.BadRequestError
import ru.practicum.android.diploma.common.domain.NoInternetError
import ru.practicum.android.diploma.common.domain.ServerInternalError
import ru.practicum.android.diploma.filters.choosearea.data.dto.AreasCatalogDto
import ru.practicum.android.diploma.filters.choosearea.data.dto.AreasCatalogRequest
import ru.practicum.android.diploma.filters.choosearea.data.dto.AreasCatalogResponse
import ru.practicum.android.diploma.filters.choosearea.data.dto.AreasRequest
import ru.practicum.android.diploma.filters.choosearea.domain.models.AreasNotFoundType
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
                    is AreasCatalogRequest -> {
                        val response = hhApiServiceAreas.getAreasByParentId(dto.areaId)
                        resultByResponseCode(response.code(), convertAreasCatalogDto(response.body()))
                    }

                    is AreasRequest -> {
                        val response = hhApiServiceAreas.getAreas()
                        resultByResponseCode(response.code(), convertAreasCatalogResponse(response.body()))
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

    private fun resultByResponseCode(code: Int, okResult: ResponseBase): ResponseBase {
        return when (code) {
            HttpURLConnection.HTTP_OK -> okResult
            HttpURLConnection.HTTP_NOT_FOUND -> ResponseBase(AreasNotFoundType())
            else -> ResponseBase(BadRequestError())
        }
    }

    private fun convertAreasCatalogResponse(responseBody: List<AreasCatalogDto>?): ResponseBase =
        if (responseBody == null) {
            ResponseBase(AreasNotFoundType())
        } else {
            AreasCatalogResponse(
                responseBody
            )
        }

    private fun convertAreasCatalogDto(responseBody: AreasCatalogDto?): ResponseBase =
        responseBody ?: ResponseBase(AreasNotFoundType())
}
