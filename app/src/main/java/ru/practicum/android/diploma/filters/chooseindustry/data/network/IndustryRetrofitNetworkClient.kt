package ru.practicum.android.diploma.filters.chooseindustry.data.network

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
import ru.practicum.android.diploma.filters.chooseindustry.data.dto.IndustryDto
import ru.practicum.android.diploma.filters.chooseindustry.data.dto.IndustryResponse
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustryErrorType
import ru.practicum.android.diploma.util.isConnected
import java.io.IOException
import java.net.HttpURLConnection

class IndustryRetrofitNetworkClient(val api: HhApiServiceIndustry, val context: Context) : NetworkClient {
    override suspend fun doRequest(dto: Any): ResponseBase {
        val response = api.getIndustries()
        if (!isConnected(context)) {
            return ResponseBase(NoInternetError())
        }
        return withContext(Dispatchers.IO) {
            try {
                when (response.code()) {
                    HttpURLConnection.HTTP_OK -> convertIndustryResponse(response.body())
                    HttpURLConnection.HTTP_BAD_REQUEST -> ResponseBase(IndustryErrorType())
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
            ResponseBase(IndustryErrorType())
        } else {
            IndustryResponse(
                responseBody
            )
        }
}
