package ru.practicum.android.diploma.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.data.impl.FavouriteVacancyRepositoryImpl
import ru.practicum.android.diploma.favorites.domain.db.FavouriteVacancyRepository
import ru.practicum.android.diploma.filters.choosearea.data.repo.ChooseAreaRepositoryImpl
import ru.practicum.android.diploma.filters.choosearea.domain.api.ChooseAreaRepository
import ru.practicum.android.diploma.filters.chooseindustry.data.repo.IndustryRepositoryImpl
import ru.practicum.android.diploma.filters.chooseindustry.domain.interfaces.IndustryRepository
import ru.practicum.android.diploma.filters.settingsfilters.data.repo.SettingsRepositoryImpl
import ru.practicum.android.diploma.filters.settingsfilters.domain.api.SettingsRepository
import ru.practicum.android.diploma.search.data.repo.SearchRepositoryImpl
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.vacancydetails.data.repo.DetailsRepositoryImpl
import ru.practicum.android.diploma.vacancydetails.data.repo.ExternalNavigatorImpl
import ru.practicum.android.diploma.vacancydetails.domain.api.DetailsRepository
import ru.practicum.android.diploma.vacancydetails.domain.api.ExternalNavigator

val repositoryModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(get(named(DiConstants.SEARCH)))
    }
    single<FavouriteVacancyRepository> {
        FavouriteVacancyRepositoryImpl(get(), get())
    }
    single<DetailsRepository> {
        DetailsRepositoryImpl(get(named(DiConstants.DETAILS)))
    }
    single<ExternalNavigator> {
        ExternalNavigatorImpl()
    }
    single<ChooseAreaRepository> {
        ChooseAreaRepositoryImpl(get(named(DiConstants.AREAS)), get())
    }
    single<IndustryRepository> {
        IndustryRepositoryImpl(get(named(DiConstants.INDUSTRY)))
    }
    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
}
