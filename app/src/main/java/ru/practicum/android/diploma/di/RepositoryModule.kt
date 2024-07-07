package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.search.data.repo.SearchRepositoryImpl
import ru.practicum.android.diploma.search.domain.api.SearchRepository

val repositoryModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(get())
    }
}
