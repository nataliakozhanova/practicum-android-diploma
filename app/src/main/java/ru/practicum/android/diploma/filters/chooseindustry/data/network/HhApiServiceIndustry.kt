package ru.practicum.android.diploma.filters.chooseindustry.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.practicum.android.diploma.filters.chooseindustry.data.dto.IndustryDto

interface HhApiServiceIndustry {
    @GET("/industries")
    suspend fun getIndustries(@Query("search") query: String? = null): Response<List<IndustryDto>>
}
