package ru.practicum.android.diploma.filters.chooseindustry.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import ru.practicum.android.diploma.filters.chooseindustry.data.dto.IndustryResponse

interface HhApiServiceIndustry {
    @Headers(
        USER
    )
    @GET("/industries")
    suspend fun getIndustries(): Response<IndustryResponse>

    companion object {
        const val USER = "HH-User-Agent: PracticumHHCareerCompass (natalia.v.kozhanova@gmail.com)"
    }
}
