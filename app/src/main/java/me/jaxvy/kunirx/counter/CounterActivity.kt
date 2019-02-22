package me.jaxvy.kunirx.counter

import android.os.Bundle
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_counter.*
import me.jaxvy.kunirx.R
import me.jaxvy.kunirx.architecture.UIAction
import me.jaxvy.kunirx.architecture.UIState
import me.jaxvy.kunirx.architecture.UIView
import me.jaxvy.kunirx.di.inject
import javax.inject.Inject

data class CounterActivityUIState(
        val counterValue: Int = 0
) : UIState

class CounterActivity : UIView<CounterActivityUIState>() {

    override lateinit var uiState: CounterActivityUIState

    @Inject
    override lateinit var renderer: CounterActivityRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        uiState = CounterActivityUIState(
                counterValue = 1
        )
    }

    override fun render(uiState: CounterActivityUIState) {
        counter.text = uiState.counterValue.toString()
    }

    override fun uiActionDataObservable(): Observable<UIAction.UIActionData> {
        return Observable.mergeArray(
                counter.clicks()
                        .map { CounterClickUIAction.Data(currentValue = uiState.counterValue) }
        )
    }
}