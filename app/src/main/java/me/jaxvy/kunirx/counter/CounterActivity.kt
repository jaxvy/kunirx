package me.jaxvy.kunirx.counter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.Scheduler
import kotlinx.android.synthetic.main.activity_counter.*
import me.jaxvy.kunirx.R
import me.jaxvy.kunirx.architecture.Renderer
import me.jaxvy.kunirx.architecture.UIEvent
import me.jaxvy.kunirx.architecture.UIState
import me.jaxvy.kunirx.di.IOScheduler
import me.jaxvy.kunirx.di.MainScheduler
import me.jaxvy.kunirx.di.inject
import javax.inject.Inject

data class CounterActivityUIState(
        val counterValue: Int = 0
) : UIState

class CounterClickUIEvent(val data: Int) : UIEvent

class CounterActivity : AppCompatActivity() {

    lateinit var uiState: CounterActivityUIState

    lateinit var renderer: Renderer<CounterActivityUIState>

    @field:[Inject MainScheduler]
    lateinit var mainScheduler: Scheduler

    @field:[Inject IOScheduler]
    lateinit var ioScheduler: Scheduler

    @Inject
    lateinit var counterActivityUIEventToIntentMapper: CounterActivityUIEventToIntentMapper

    override fun onCreate(savedInstanceState: Bundle?) {
        inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        uiState = CounterActivityUIState(counterValue = 1)
        renderer = Renderer(
                mainScheduler = mainScheduler,
                ioScheduler = ioScheduler,
                uiEventToIntentMapper = counterActivityUIEventToIntentMapper,
                reducer = CounterActivityReducer(),
                uiEvents = ::uiEvents,
                callback = ::render,
                uiState = uiState
        )
    }

    private fun render(uiState: CounterActivityUIState) {
        this.uiState = uiState
        counter.text = uiState.counterValue.toString()
    }

    private fun uiEvents(): Observable<UIEvent> {
        return Observable.mergeArray(
                counter.clicks()
                        .map { CounterClickUIEvent(data = uiState.counterValue) }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        renderer.stop()
    }
}
