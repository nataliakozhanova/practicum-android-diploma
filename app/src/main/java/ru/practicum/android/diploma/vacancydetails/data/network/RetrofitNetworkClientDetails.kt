package ru.practicum.android.diploma.vacancydetails.data.network

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
import ru.practicum.android.diploma.util.isConnected

class RetrofitNetworkClientDetails(
    private val hhApiServiceDetails: HhApiServiceDetails,
    private val context: Context,
) : NetworkClient {

    companion object {
        const val SERVER_ERROR = 500
    }

    override suspend fun doRequest(dto: Any): ResponseBase {
        if (!isConnected(context)) {
            return ResponseBase(NoInternetError())
        }
        return withContext(Dispatchers.IO) {
            try {
                hhApiServiceDetails.getVacancyDetails(dto.toString())
            } catch (e: HttpException) {
                when (e.code()) {
                    SERVER_ERROR -> ResponseBase(ServerInternalError())
                    else -> ResponseBase(BadRequestError())
                }
            } catch (e: HttpException) {
                Log.e("Http", e.toString())
                ResponseBase(NoInternetError())
            }
        }
    }
}
