package ru.practicum.android.diploma.common.domain

abstract class VacancyBase(
val id: Int,            //  Внутренний ИД БД
val hhID: String,       // ИД HH для запросов
val name: String,       // Название вакансии
val isFavorite: Boolean // Флаг Избранное
)
