package me.jaxvy.kunirx.architecture

import android.util.Log
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import me.jaxvy.kunirx.di.IOScheduler
import me.jaxvy.kunirx.di.MainScheduler
import javax.inject.Inject

class Renderer<U : UIState>(
        private val mainScheduler: Scheduler,
        private val ioScheduler: Scheduler
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
                    (uiView.uiActionListProvider().uiActions() as? List<UIAction<U, UIAction.UIActionData>>)
                            ?.find { uiAction ->
                                isUIActionDataBelongsToUIAction(uiActionData, uiAction)
                            }?.let { uiAction ->
                                uiAction.execute(uiActionData)
                                        .map { mutator -> uiAction.reduce(uiView.uiState, mutator) }
                                        .subscribeOn(ioScheduler)
                            } ?: Observable.just(uiView.uiState)
                }
                .observeOn(mainScheduler)
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
