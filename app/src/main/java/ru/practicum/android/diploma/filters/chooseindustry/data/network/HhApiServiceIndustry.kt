package ru.practicum.android.diploma.filters.chooseindustry.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.filters.chooseindustry.data.dto.IndustryDto

interface HhApiServiceIndustry {
    @Headers(
        USER,
        AUTH
    )
    @GET("/industries")
    suspend fun getIndustries(@Query("search") query: String? = null): Response<List<IndustryDto>>

    companion object {
        const val USER = "HH-User-Agent: PracticumHHCareerCompass (natalia.v.kozhanova@gmail.com)"
        const val AUTH = "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}"
    }
}
