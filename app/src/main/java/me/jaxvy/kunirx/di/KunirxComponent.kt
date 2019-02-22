package me.jaxvy.kunirx.di

import dagger.Component
import me.jaxvy.kunirx.KunirxApplication
import me.jaxvy.kunirx.counter.CounterActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [KunirxModule::class])
interface KunirxComponent {

    fun inject(counterActivity: CounterActivity)
}

fun CounterActivity.inject(counterActivity: CounterActivity) {
    (applicationContext as KunirxApplication).kunirxComponent.inject(counterActivity)
}
