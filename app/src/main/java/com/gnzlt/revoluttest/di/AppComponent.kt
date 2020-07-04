package com.gnzlt.revoluttest.di

import com.gnzlt.revoluttest.rates.RatesActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ViewModelModule::class,
        NetworkModule::class
    ]
)
interface AppComponent {

    fun inject(activity: RatesActivity)
}
