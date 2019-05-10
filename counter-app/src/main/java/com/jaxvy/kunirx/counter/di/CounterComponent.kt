package com.jaxvy.kunirx.counter.di

import com.jaxvy.kunirx.counter.CounterApplication
import com.jaxvy.kunirx.counter.ui.CounterActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [CounterModule::class])
interface CounterComponent {

    fun inject(counterActivity: CounterActivity)
}

fun CounterActivity.inject(counterActivity: CounterActivity) {
    (applicationContext as CounterApplication).counterComponent.inject(counterActivity)
}
