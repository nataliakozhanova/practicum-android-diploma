package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.domain.db.FavouriteVacancyInteractor
import ru.practicum.android.diploma.favorites.domain.impl.FavouriteVacancyInteractorImpl
import ru.practicum.android.diploma.filters.choosearea.domain.api.ChooseAreaInteractor
import ru.practicum.android.diploma.filters.choosearea.domain.impl.ChooseAreaInteractorImpl
import ru.practicum.android.diploma.filters.chooseindustry.domain.impl.IndustryInteractorImpl
import ru.practicum.android.diploma.filters.chooseindustry.domain.interfaces.IndustryInteractor
import ru.practicum.android.diploma.filters.settingsfilters.domain.api.SettingsInteractor
import ru.practicum.android.diploma.filters.settingsfilters.domain.impl.SettingsInteractorImpl
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.impl.SearchInteractorImpl
import ru.practicum.android.diploma.vacancydetails.domain.api.DetailsInteractor
import ru.practicum.android.diploma.vacancydetails.domain.impl.DetailsInteractorImpl

val interactorModule = module {
    factory<SearchInteractor> {
        SearchInteractorImpl(get())
    }
    factory<FavouriteVacancyInteractor> {
        FavouriteVacancyInteractorImpl(get())
    }
    factory<DetailsInteractor> {
        DetailsInteractorImpl(get(), get())
    }
    factory<ChooseAreaInteractor> {
        ChooseAreaInteractorImpl(get())
    }
    factory<IndustryInteractor> {
        IndustryInteractorImpl(get())
    }
    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }
}
