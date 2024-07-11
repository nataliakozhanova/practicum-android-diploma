package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.presentation.viewmodel.FavouritesViewModel
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchViewModel

val viewModelModule = module {
    viewModel {
        SearchViewModel(get())
    }
    viewModel {
        FavouritesViewModel(get())
    }
}
