package ru.practicum.android.diploma.search.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse

interface HhApiService {
    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: PracticumHHCareerCompass (natalia.v.kozhanova@gmail.com)"
    )
    @GET("/vacancies")
    suspend fun findVacancies(@QueryMap options: Map<String, String>): VacancySearchResponse
}
