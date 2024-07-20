package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.presentation.viewmodel.FavouritesViewModel
import ru.practicum.android.diploma.filters.choosearea.presentation.viewmodel.ChooseAreaViewModel
import ru.practicum.android.diploma.filters.choosearea.presentation.viewmodel.ChooseCountryViewModel
import ru.practicum.android.diploma.filters.choosearea.presentation.viewmodel.ChooseRegionViewModel
import ru.practicum.android.diploma.filters.settingsfilters.presentation.viewmodel.SettingsFiltersViewModel
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchViewModel
import ru.practicum.android.diploma.vacancydetails.presentation.viewmodel.DetailsViewModel

val viewModelModule = module {
    viewModel {
        SearchViewModel(androidContext(), get(), get(), get())
    }
    viewModel {
        FavouritesViewModel(get())
    }
    viewModel {
        DetailsViewModel(get(), get())
    }
    viewModel {
        ChooseAreaViewModel(get())
    }
    viewModel {
        ChooseCountryViewModel(get())
    }
    viewModel {
        ChooseRegionViewModel(get())
    }
    viewModel {
        SettingsFiltersViewModel(get(), get(), get())
    }
}
