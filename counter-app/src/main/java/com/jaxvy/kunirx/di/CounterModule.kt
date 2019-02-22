package com.jaxvy.kunirx.di

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IOScheduler

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MainScheduler

@Module
class CounterModule {

    @Singleton
    @Provides
    @IOScheduler
    fun provideIOScheduler(): Scheduler {
        return Schedulers.io()
    }

    @Singleton
    @MainScheduler
    @Provides
    fun provideMainScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}
