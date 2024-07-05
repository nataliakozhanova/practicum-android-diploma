package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import ru.practicum.android.diploma.data.dto.Response

interface HhApiService {
    @Headers("Authorization: Bearer YOUR_TOKEN",
        "HH-User-Agent: Application Name (name@example.com)")
    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancy(@Path("vacancy_id") id: String): Response
}
