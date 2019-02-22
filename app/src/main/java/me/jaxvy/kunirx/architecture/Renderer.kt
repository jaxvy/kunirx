package me.jaxvy.kunirx.architecture

import android.util.Log
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

class Renderer<U : UIState>(
        private val mainScheduler: Scheduler,
        private val ioScheduler: Scheduler,
        private val uiEventToIntentMapper: UIEventToIntentMapper,
        private val reducer: Reducer<U>,
        private val uiEvents: () -> Observable<UIEvent>,
        private val callback: (U) -> Unit,
        private var uiState: U
) {
    private val compositeDisposable = CompositeDisposable()

    /**
     * TODO: Handle these
     * 1. Errors
     * 2. Logging
     */
    init {
        callback(uiState)
        compositeDisposable.add(
                uiEvents()
                        .publish {
                            uiEventToIntentMapper.map(it)
                                    .subscribeOn(ioScheduler)
                        }
                        .map { intentResult -> reducer.reduce(uiState, intentResult) }
                        .observeOn(mainScheduler)
                        .subscribe(
                                { newUIState ->
                                    callback(newUIState)
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
}

interface UIEventToIntentMapper {
    fun map(uiEvent: Observable<UIEvent>): Observable<Intent.Result>
}

interface Reducer<U : UIState> {
    fun reduce(state: U, intentResult: Intent.Result?): U
}
