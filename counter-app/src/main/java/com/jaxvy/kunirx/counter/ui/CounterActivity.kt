package com.jaxvy.kunirx.counter.ui

import android.os.Bundle
import com.jakewharton.rxbinding3.view.clicks
import com.jaxvy.kunirx.UIAction
import com.jaxvy.kunirx.UIActionConfig
import com.jaxvy.kunirx.UIState
import com.jaxvy.kunirx.UIViewActivity
import com.jaxvy.kunirx.counter.R
import com.jaxvy.kunirx.counter.di.IOScheduler
import com.jaxvy.kunirx.counter.di.MainScheduler
import com.jaxvy.kunirx.counter.di.inject
import dagger.Reusable
import io.reactivex.Observable
import io.reactivex.Scheduler
import kotlinx.android.synthetic.main.activity_counter.*
import javax.inject.Inject

data class CounterActivityUIState(
    val counterValue: Int = 0
) : UIState

@Reusable
class CounterActivityUIActionConfig @Inject constructor(
    @MainScheduler override var mainScheduler: Scheduler,
    @IOScheduler override var ioScheduler: Scheduler,
    incrementClickUIAction: IncrementClickUIAction,
    decrementClickUIAction: DecrementClickUIAction
) : UIActionConfig(
    uiActions = listOf(
        incrementClickUIAction,
        decrementClickUIAction
    )
)

// Activity extends from UIViewActivity which provides the necessary logic to execute actions and
// handle the results.
class CounterActivity : UIViewActivity<CounterActivityUIState>() {

    @Inject
    lateinit var uiActionConfig: CounterActivityUIActionConfig

    override fun uiActionConfig() = uiActionConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        // Note that although we use dagger here, it is not mandatory for kunirx to work.
        inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        // Initialize the uiState.
        uiState = CounterActivityUIState()
    }

    // Using RxBindings to react to UIActions.
    override fun react(): Observable<UIAction.Input> {
        return Observable.mergeArray(
            incrementButton
                .clicks()
                .map { IncrementClickUIAction.Input(currentValue = uiState.counterValue) },

            decrementButton
                .clicks()
                .map { DecrementClickUIAction.Input(currentValue = uiState.counterValue) }
        )
    }

    // This is the callback which for the given "state" the view is always rendered consistently.
    override fun render(uiState: CounterActivityUIState) {
        counter.text = uiState.counterValue.toString()
    }
}
