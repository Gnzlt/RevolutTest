package com.gnzlt.revoluttest

import android.app.Application
import com.gnzlt.revoluttest.di.AppComponent
import com.gnzlt.revoluttest.di.DaggerAppComponent

class RevolutApp : Application() {

    val appComponent: AppComponent = DaggerAppComponent.create()
}
