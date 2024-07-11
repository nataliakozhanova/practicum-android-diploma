package ru.practicum.android.diploma.vacancydetails.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.common.data.NetworkClient
import ru.practicum.android.diploma.common.data.ResponseBase
import ru.practicum.android.diploma.common.domain.BadRequestError
import ru.practicum.android.diploma.common.domain.NoInternetError
import ru.practicum.android.diploma.common.domain.ServerInternalError
import ru.practicum.android.diploma.common.domain.Success

class RetrofitNetworkClientDetails(
    private val hhApiServiceDetails: HhApiServiceDetails,
    private val context: Context
) : NetworkClient {

    companion object {
        const val SERVER_ERROR = 500
    }

    override suspend fun doRequest(dto: Any): ResponseBase {
        if (!isConnected()) {
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

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        return capabilities != null && (
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            )
    }
}
