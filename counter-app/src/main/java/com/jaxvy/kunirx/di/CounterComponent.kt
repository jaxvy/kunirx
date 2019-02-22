package com.jaxvy.kunirx.di

import dagger.Component
import com.jaxvy.kunirx.CounterApplication
import com.jaxvy.kunirx.counter.CounterActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [CounterModule::class])
interface CounterComponent {

    fun inject(counterActivity: CounterActivity)
}

fun CounterActivity.inject(counterActivity: CounterActivity) {
    (applicationContext as CounterApplication).counterComponent.inject(counterActivity)
}
