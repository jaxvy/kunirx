package com.jaxvy.kunirx.counter

import android.app.Application
import com.jaxvy.kunirx.counter.di.CounterComponent
import com.jaxvy.kunirx.counter.di.CounterModule
import com.jaxvy.kunirx.counter.di.DaggerCounterComponent

class CounterApplication : Application() {

    lateinit var counterComponent: CounterComponent

    override fun onCreate() {
        super.onCreate()

        counterComponent = DaggerCounterComponent.builder()
            .counterModule(CounterModule())
            .build()
    }
}
