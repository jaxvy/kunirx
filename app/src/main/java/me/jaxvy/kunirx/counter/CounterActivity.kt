package me.jaxvy.kunirx.counter

import android.os.Bundle
import com.jakewharton.rxbinding3.view.clicks
import dagger.Reusable
import io.reactivex.Observable
import io.reactivex.Scheduler
import kotlinx.android.synthetic.main.activity_counter.*
import me.jaxvy.kunirx.R
import me.jaxvy.kunirx.architecture.RendererConfig
import me.jaxvy.kunirx.architecture.UIAction
import me.jaxvy.kunirx.architecture.UIState
import me.jaxvy.kunirx.architecture.UIViewActivity
import me.jaxvy.kunirx.di.IOScheduler
import me.jaxvy.kunirx.di.MainScheduler
import me.jaxvy.kunirx.di.inject
import javax.inject.Inject

data class CounterActivityUIState(
        val counterValue: Int = 0
) : UIState

@Reusable
class CounterActivityRendererConfig @Inject constructor(
        private val incrementClickUIAction: IncrementClickUIAction,
        private val decrementClickUIAction: DecrementClickUIAction
) : RendererConfig<CounterActivityUIState> {

    @field:[Inject MainScheduler]
    override lateinit var mainScheduler: Scheduler

    @field:[Inject IOScheduler]
    override lateinit var ioScheduler: Scheduler

    override fun uiActions(): List<UIAction<CounterActivityUIState, *>> {
        return listOfNotNull(
                incrementClickUIAction,
                decrementClickUIAction
        )
    }
}

class CounterActivity : UIViewActivity<CounterActivityUIState>() {

    @Inject
    lateinit var rendererConfig: CounterActivityRendererConfig

    override fun rendererConfig() = rendererConfig

    override fun uiActionDataObservable(): Observable<UIAction.UIActionData> {
        return Observable.mergeArray(
                incrementButton
                        .clicks()
                        .map { IncrementClickUIAction.Data(currentValue = uiState.counterValue) },

                decrementButton
                        .clicks()
                        .map { DecrementClickUIAction.Data(currentValue = uiState.counterValue) }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        uiState = CounterActivityUIState()
    }

    override fun render(uiState: CounterActivityUIState) {
        counter.text = uiState.counterValue.toString()
    }
}
