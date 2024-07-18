package ru.practicum.android.diploma.vacancydetails.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.vacancydetails.data.dto.VacancyDetailsResponse

interface HhApiServiceDetails {
    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: PracticumHHCareerCompass (natalia.v.kozhanova@gmail.com)"
    )
    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancyDetails(@Path("vacancy_id") id: String): Response<VacancyDetailsResponse>
}
