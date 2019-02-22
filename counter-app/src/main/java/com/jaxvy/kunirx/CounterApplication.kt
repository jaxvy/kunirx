package com.jaxvy.kunirx

import android.app.Application
import com.jaxvy.kunirx.di.DaggerCounterComponent
import com.jaxvy.kunirx.di.CounterComponent
import com.jaxvy.kunirx.di.CounterModule

class CounterApplication : Application() {

    lateinit var counterComponent: CounterComponent

    override fun onCreate() {
        super.onCreate()

        counterComponent = DaggerCounterComponent.builder()
                .counterModule(CounterModule())
                .build()
    }
}
