package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.domain.db.FavouriteVacancyInteractor
import ru.practicum.android.diploma.favorites.domain.impl.FavouriteVacancyInteractorImpl
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.impl.SearchInteractorImpl
import ru.practicum.android.diploma.sharingandcontactvacancy.domain.api.SharingContactInteractor
import ru.practicum.android.diploma.sharingandcontactvacancy.domain.impl.SharingContactInteractorImpl
import ru.practicum.android.diploma.vacancydetails.domain.api.DetailsInteractor
import ru.practicum.android.diploma.vacancydetails.domain.impl.DetailsInteractorImpl

val interactorModule = module {
    factory<SearchInteractor> {
        SearchInteractorImpl(get())
    }
    single<FavouriteVacancyInteractor> {
        FavouriteVacancyInteractorImpl(get())
    }
    single<DetailsInteractor> {
        DetailsInteractorImpl(get())
    }
    factory<SharingContactInteractor> { (hhVacancyLink: String, phone: String, email: String) ->
        SharingContactInteractorImpl(get(), hhVacancyLink, phone, email)
    }

}
