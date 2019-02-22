package me.jaxvy.kunirx.architecture

import android.util.Log
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class Renderer<U : UIState>(
        private val rendererConfig: RendererConfig<U>
) {
    private lateinit var compositeDisposable: CompositeDisposable

    /**
     * TODO: Handle
     * 1. Errors
     * 2. Logging
     */
    fun start(uiView: UIView<U>) {
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(run(uiView))
    }

    private fun run(uiView: UIView<U>): Disposable {
        return uiView.uiActionDataObservable()
                .flatMap { uiActionData ->
                    (rendererConfig.uiActions() as? List<UIAction<U, UIAction.UIActionData>>)
                            ?.find { uiAction ->
                                isUIActionDataBelongsToUIAction(uiActionData, uiAction)
                            }?.let { uiAction ->
                                uiAction.execute(uiActionData)
                                        .map { mutator -> uiAction.reduce(uiView.uiState, mutator) }
                                        .subscribeOn(rendererConfig.ioScheduler)
                            } ?: Observable.just(uiView.uiState)
                }
                .observeOn(rendererConfig.mainScheduler)
                .subscribe(
                        { newUIState ->
                            uiView.apply {
                                uiState = newUIState
                                render(newUIState)
                            }
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

    private fun isUIActionDataBelongsToUIAction(
            uiActionData: UIAction.UIActionData,
            uiAction: UIAction<*, *>
    ): Boolean = uiActionData::class.java.declaringClass == uiAction::class.java
}

interface RendererConfig<U : UIState> {
    val mainScheduler: Scheduler

    val ioScheduler: Scheduler

    fun uiActions(): List<UIAction<U, *>>
}
