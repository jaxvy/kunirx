package me.jaxvy.kunirx.architecture

import android.util.Log
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import me.jaxvy.kunirx.di.IOScheduler
import me.jaxvy.kunirx.di.MainScheduler
import javax.inject.Inject

abstract class Renderer<U : UIState> {
    private lateinit var compositeDisposable: CompositeDisposable

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
        compositeDisposable.add(
                uiView.uiEvents()
                        .publish {
                            mapUIEventToIntent(it)
                                    .subscribeOn(ioScheduler)
                        }
                        .map { intentResult -> reduce(uiView.uiState, intentResult) }
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
        )
    }

    fun stop() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    abstract fun mapUIEventToIntent(uiEvent: Observable<UIEvent>): Observable<Intent.Result>

    abstract fun reduce(state: U, intentResult: Intent.Result?): U
}
