package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.search.presentation.view_model.SearchViewModel

val viewModelModule = module {
    viewModel {
        SearchViewModel(androidContext(), get())
    }
}
