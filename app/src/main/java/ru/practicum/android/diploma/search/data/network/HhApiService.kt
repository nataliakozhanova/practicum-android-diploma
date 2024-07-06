package ru.practicum.android.diploma.search.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse

interface HhApiService {
    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: PracticumHHCareerCompass (natalia.v.kozhanova@gmail.com)"
    )
    @GET("/vacancies")
    suspend fun findVacancies(
        @Query("search_field") searchField: String,
        @Query("text") text: String,
    ): VacancySearchResponse


    /*@GET("/vacancies/{vacancy_id}")
    suspend fun getVacancy(@Path("vacancy_id") id: String): Response*/
}
