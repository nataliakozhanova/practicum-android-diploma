package ru.practicum.android.diploma.filters.choosearea.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.filters.choosearea.data.dto.AreasCatalogDto
import ru.practicum.android.diploma.filters.choosearea.data.dto.AreasCatalogResponse

interface HhApiServiceAreas {
    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: PracticumHHCareerCompass (natalia.v.kozhanova@gmail.com)"
    )
    @GET("/areas")
    suspend fun getAreas(): Response<AreasCatalogResponse>

    @GET("/areas/{area_id}")
    suspend fun getAreasByParentId(@Path("area_id") areaId: String?): Response<AreasCatalogDto>

}
