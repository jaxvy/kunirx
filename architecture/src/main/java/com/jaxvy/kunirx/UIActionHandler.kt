package com.jaxvy.kunirx

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
        return uiView.uiActionInputObservable()
            .flatMap { input ->
                findUIActionByInput(uiView.uiActionHandlerConfiguration().uiActions, input)
                    ?.let { uiAction ->
                        uiAction.execute(input)
                            .map { mutator -> uiAction.reduce(uiView.uiState, mutator) }
                            .subscribeOn(configuration.ioScheduler)
                    }
                    ?: Observable.error<U>(
                        Throwable(
                            "$input not found in uiActionList, are you sure it's defined in " +
                                    "your view's UIActionHandler.Configuration?"
                        )
                    )

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
                    Log.e("UIActionHandler: ", error.message)
                }
            )
    }

    fun stop() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    // Some reflection magic to match a given Input type to its corresponding UIAction. Assumes the
    // Input class is defined inside its corresponding UIAction.
    private fun findUIActionByInput(
        uiActions: List<UIAction<*, *, *>>,
        input: UIAction.Input
    ): UIAction<U, UIAction.Input, UIAction.UIStateMutator>? {
        return (uiActions as? List<UIAction<U, UIAction.Input, UIAction.UIStateMutator>>)
            ?.find { uiAction -> input::class.java.declaringClass == uiAction::class.java }
    }

    // Helps initialize the UIActionHandler with provided Schedulers and uiActions
    abstract class Configuration(val uiActions: List<UIAction<*, *, *>> = emptyList()) {
        abstract val mainScheduler: Scheduler

        abstract val ioScheduler: Scheduler
    }
}
