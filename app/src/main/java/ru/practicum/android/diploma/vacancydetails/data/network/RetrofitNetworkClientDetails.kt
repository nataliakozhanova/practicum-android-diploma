package ru.practicum.android.diploma.vacancydetails.data.network

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
import ru.practicum.android.diploma.util.isConnected
import ru.practicum.android.diploma.vacancydetails.data.dto.VacancyDetailsRequest
import ru.practicum.android.diploma.vacancydetails.data.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.vacancydetails.domain.models.DetailsNotFoundType
import java.net.HttpURLConnection

class RetrofitNetworkClientDetails(
    private val hhApiServiceDetails: HhApiServiceDetails,
    private val context: Context,
) : NetworkClient {

    override suspend fun doRequest(dto: Any): ResponseBase {
        if (!isConnected(context)) {
            return ResponseBase(NoInternetError())
        }
        return withContext(Dispatchers.IO) {
            try {
                when (dto) {
                    is VacancyDetailsRequest -> {
                        val response = hhApiServiceDetails.getVacancyDetails(dto.vacancyID)
                        when (response.code()) {
                            HttpURLConnection.HTTP_OK -> {
                                val vacancyDetailsResponse = response.body()
                                if (vacancyDetailsResponse == null) {
                                    ResponseBase(DetailsNotFoundType())
                                } else {
                                    VacancyDetailsResponse(
                                        vacancyDetailsResponse.id,
                                        vacancyDetailsResponse.name,
                                        vacancyDetailsResponse.area,
                                        vacancyDetailsResponse.address,
                                        vacancyDetailsResponse.employer,
                                        vacancyDetailsResponse.salary,
                                        vacancyDetailsResponse.experience,
                                        vacancyDetailsResponse.employment,
                                        vacancyDetailsResponse.schedule,
                                        vacancyDetailsResponse.description,
                                        vacancyDetailsResponse.keySkill,
                                        vacancyDetailsResponse.contacts,
                                        vacancyDetailsResponse.hhVacancyLink
                                    )
                                }

                            }

                            HttpURLConnection.HTTP_NOT_FOUND -> ResponseBase(DetailsNotFoundType())
                            else -> ResponseBase(BadRequestError())
                        }
                    }

                    else -> ResponseBase(BadRequestError())
                }

            } catch (e: HttpException) {
                e.message?.let { Log.e("Http", it) }
                ResponseBase(ServerInternalError())
            }
        }
    }
}

//    companion object {
//        const val SERVER_ERROR = 500
//    }
//
//    override suspend fun doRequest(dto: Any): ResponseBase {
//        if (!isConnected(context)) {
//            return ResponseBase(NoInternetError())
//        }
//        return withContext(Dispatchers.IO) {
//            try {
//                hhApiServiceDetails.getVacancyDetails(dto.toString())
//            } catch (e: HttpException) {
//                when (e.code()) {
//                    SERVER_ERROR -> ResponseBase(ServerInternalError())
//                    else -> ResponseBase(BadRequestError())
//                }
//            } catch (e: HttpException) {
//                Log.e("Http", e.toString())
//                ResponseBase(NoInternetError())
//            }
//        }
//    }
