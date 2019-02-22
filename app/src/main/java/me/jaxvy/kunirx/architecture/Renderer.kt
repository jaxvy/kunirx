package me.jaxvy.kunirx.architecture

import android.util.Log
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import me.jaxvy.kunirx.di.IOScheduler
import me.jaxvy.kunirx.di.MainScheduler
import javax.inject.Inject

abstract class Renderer<U : UIState> {
    private lateinit var compositeDisposable: CompositeDisposable

    abstract fun uiActions(): List<UIAction<U, UIEvent>>

    @field:[Inject MainScheduler]
    lateinit var mainScheduler: Scheduler

    @field:[Inject IOScheduler]
    lateinit var ioScheduler: Scheduler

    /**
     * TODO: Handle these
     * 1. Errors
     * 2. Logging
     */
    fun start(uiView: UIView<U>) {
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(run(uiView))
    }

    private fun run(uiView: UIView<U>): Disposable {
        return uiView.uiActionEvents()
                .flatMap { uiEvent ->
                    uiActions().find {
                        it.uiEvent == uiEvent::class.java
                    }?.let { uiAction ->
                        uiAction.execute(uiEvent)
                                .map { mutator -> uiAction.reduce(uiView.uiState, mutator) }
                                .subscribeOn(ioScheduler)
                    } ?: Observable.just(uiView.uiState)
                }
                .observeOn(mainScheduler)
                .subscribe(
                        { newUIState ->
                            uiView.render(newUIState)
                                    .also { uiView.uiState = newUIState }
                        },
                        { error ->
                            Log.e("Renderer error", error.message)
                        }
                )
    }

    fun stop() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}
