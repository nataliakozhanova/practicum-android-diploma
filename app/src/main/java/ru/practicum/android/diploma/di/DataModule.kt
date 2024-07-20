package ru.practicum.android.diploma.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.data.NetworkClient
import ru.practicum.android.diploma.favorites.data.converters.VacancyDbConverter
import ru.practicum.android.diploma.favorites.data.db.VacancyDatabase
import ru.practicum.android.diploma.filters.choosearea.data.network.AreasRetrofitNetworkClient
import ru.practicum.android.diploma.filters.choosearea.data.network.HhApiServiceAreas
import ru.practicum.android.diploma.filters.choosearea.data.storage.AreasStorageApi
import ru.practicum.android.diploma.filters.choosearea.data.storage.AreasStorageImpl
import ru.practicum.android.diploma.filters.chooseindustry.data.network.HhApiServiceIndustry
import ru.practicum.android.diploma.filters.chooseindustry.data.network.IndustryRetrofitNetworkClient
import ru.practicum.android.diploma.search.data.network.HhApiService
import ru.practicum.android.diploma.search.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.vacancydetails.data.network.HhApiServiceDetails
import ru.practicum.android.diploma.vacancydetails.data.network.RetrofitNetworkClientDetails

val dataModule = module {
    single {
        androidContext()
            .getSharedPreferences(DiConstants.PREFERENCES, Context.MODE_PRIVATE)
    }

    single<AreasStorageApi> {
        AreasStorageImpl(get())
    }

    single<HhApiService> {
        Retrofit.Builder()
            .baseUrl(DiConstants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HhApiService::class.java)
    }

    single<HhApiServiceDetails> {
        Retrofit.Builder()
            .baseUrl(DiConstants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HhApiServiceDetails::class.java)
    }

    single<HhApiServiceAreas> {
        Retrofit.Builder()
            .baseUrl(DiConstants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HhApiServiceAreas::class.java)
    }

    single<HhApiServiceIndustry> {
        Retrofit.Builder()
            .baseUrl(DiConstants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HhApiServiceIndustry::class.java)
    }

    single {
        Room.databaseBuilder(androidContext(), VacancyDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        androidContext().getSharedPreferences(
            androidContext().getString(R.string.shared_pref_storage_key),
            Context.MODE_PRIVATE
        )
    }
    factory { VacancyDbConverter() }

    factory { Gson() }

    single<NetworkClient>(named(DiConstants.SEARCH)) {
        RetrofitNetworkClient(get(), androidContext())
    }
    single<NetworkClient>(named(DiConstants.DETAILS)) {
        RetrofitNetworkClientDetails(get(), androidContext())
    }

    single<NetworkClient>(named(DiConstants.AREAS)) {
        AreasRetrofitNetworkClient(get(), androidContext())
    }
    single<NetworkClient>(named(DiConstants.INDUSTRY)) {
        IndustryRetrofitNetworkClient(get(), androidContext())
    }

}
