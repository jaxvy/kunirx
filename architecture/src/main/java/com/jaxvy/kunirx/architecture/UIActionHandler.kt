package com.jaxvy.kunirx.architecture

import android.util.Log
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

// Executes UIActions and using the updated UIState, renders the UIViews
class UIActionHandler<U : UIState>(
        private val configuration: Configuration
) {
    private lateinit var compositeDisposable: CompositeDisposable

    fun start(uiView: UIView<U>) {
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(run(uiView))
    }

    private fun run(uiView: UIView<U>): Disposable {
        return uiView.uiActionDataObservable()
                .flatMap { uiActionData ->
                    (uiView.uiActionHandlerConfiguration().uiActions
                            as? List<UIAction<U, UIAction.Input, UIAction.UIStateMutator>>)
                            ?.find { uiAction ->
                                isUIActionDataBelongsToUIAction(uiActionData, uiAction)
                            }?.let { uiAction ->
                                uiAction.execute(uiActionData)
                                        .map { mutator -> uiAction.reduce(uiView.uiState, mutator) }
                                        .subscribeOn(configuration.ioScheduler)
                            } ?: Observable.just(uiView.uiState)
                }
                .observeOn(configuration.mainScheduler)
                .subscribe(
                        { newUIState ->
                            uiView.apply {
                                uiState = newUIState
                                render(newUIState)
                            }
                        },
                        { error ->
                            Log.e("UIActionHandler error", error.message)
                        }
                )
    }

    fun stop() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    private fun isUIActionDataBelongsToUIAction(
            input: UIAction.Input,
            uiAction: UIAction<*, *, *>
    ): Boolean = input::class.java.declaringClass == uiAction::class.java

    // Helps initialize the UIActionHandler with provided Schedulers and uiActions
    abstract class Configuration(val uiActions: List<UIAction<*, *, *>>) {
        abstract val mainScheduler: Scheduler

        abstract val ioScheduler: Scheduler
    }
}
