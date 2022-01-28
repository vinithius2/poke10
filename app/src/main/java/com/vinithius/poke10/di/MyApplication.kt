package com.vinithius.poke10.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication: Application() {

    private val appModules by lazy {
        listOf(
            repositoryModule,
            repositoryDataModule,
            viewModelModule,
            remoteDataModule
        )
    }

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger()
            androidContext(this@MyApplication)
            modules(appModules)
        }
    }

}