package ru.practicum.android.diploma.common.data

interface NetworkClient {
    suspend fun doRequest(dto: Any): ResponseBase
}
