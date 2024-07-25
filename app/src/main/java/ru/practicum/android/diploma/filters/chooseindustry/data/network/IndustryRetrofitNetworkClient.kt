package ru.practicum.android.diploma.filters.chooseindustry.data.network

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
import ru.practicum.android.diploma.filters.chooseindustry.data.dto.IndustrtesRequest
import ru.practicum.android.diploma.filters.chooseindustry.data.dto.IndustryDto
import ru.practicum.android.diploma.filters.chooseindustry.data.dto.IndustryResponse
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustryNotFoundType
import ru.practicum.android.diploma.util.isConnected
import java.io.IOException
import java.net.HttpURLConnection

class IndustryRetrofitNetworkClient(val api: HhApiServiceIndustry, val context: Context) : NetworkClient {
    override suspend fun doRequest(dto: Any): ResponseBase {
        if (!isConnected(context)) {
            return ResponseBase(NoInternetError())
        }
        val query = (dto as? IndustrtesRequest)?.query
        val response = api.getIndustries(query)

        return withContext(Dispatchers.IO) {
            try {
                when (response.code()) {
                    HttpURLConnection.HTTP_OK -> convertIndustryResponse(response.body())
                    HttpURLConnection.HTTP_BAD_REQUEST -> ResponseBase(IndustryNotFoundType())
                    else -> ResponseBase(BadRequestError())
                }
            } catch (e: HttpException) {
                e.message?.let { Log.e("Http", it) }
                ResponseBase(ServerInternalError())
            } catch (e: IOException) {
                e.message?.let { Log.e("IO", it) }
                ResponseBase(ServerInternalError())
            }
        }
    }

    private fun convertIndustryResponse(responseBody: List<IndustryDto>?): ResponseBase =
        if (responseBody == null) {
            ResponseBase(IndustryNotFoundType())
        } else {
            IndustryResponse(
                responseBody
            )
        }
}
