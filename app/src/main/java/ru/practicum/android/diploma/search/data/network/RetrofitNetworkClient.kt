package ru.practicum.android.diploma.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.search.data.NetworkClient
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.dto.VacancySearchRequest
import java.net.HttpURLConnection

class RetrofitNetworkClient(
    private val apiService: HhApiService,
    private val context: Context,
) : NetworkClient {

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        return capabilities != null
            && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }

    private fun searchOptions(dto: VacancySearchRequest): HashMap<String, String> {
        val options: HashMap<String, String> = HashMap()
        options["text"] = dto.expression
        if (dto.page != null) {
            options["page"] = dto.page.toString()
        }
        if (dto.perPage != null) {
            options["per_page"] = dto.perPage.toString()
        }
        return options
    }

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        return withContext(Dispatchers.IO) {
            try {
                when (dto) {
                    is VacancySearchRequest -> {
                        val options = searchOptions(dto)
                        apiService.findVacancies(options).apply { resultCode = HttpURLConnection.HTTP_OK }
                    }

                    else -> Response().apply { resultCode = HttpURLConnection.HTTP_BAD_REQUEST }
                }
            } catch (e: HttpException) {
                e.message?.let { Log.e("Http", it) }
                Response().apply { resultCode = HttpURLConnection.HTTP_INTERNAL_ERROR }
            }
        }
    }
}
