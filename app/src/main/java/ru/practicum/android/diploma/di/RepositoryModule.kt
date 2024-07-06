package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.search.data.api.VacanciesRepositoryImpl
import ru.practicum.android.diploma.search.domain.api.VacanciesRepository

val repositoryModule = module {
    single<VacanciesRepository> {
        VacanciesRepositoryImpl(get(), androidContext())
    }
}
