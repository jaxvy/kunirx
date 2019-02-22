package me.jaxvy.kunirx.counter

import dagger.Reusable
import io.reactivex.Observable
import me.jaxvy.kunirx.architecture.Intent
import me.jaxvy.kunirx.architecture.Renderer
import me.jaxvy.kunirx.architecture.UIEvent
import javax.inject.Inject

@Reusable
class CounterActivityRenderer @Inject constructor(
        private val incrementIntent: IncrementIntent
) : Renderer<CounterActivityUIState>() {

    override fun mapUIEventToIntent(uiEvent: Observable<UIEvent>): Observable<Intent.Result> {
        return Observable.mergeArray(
                uiEvent.ofType(CounterClickUIEvent::class.java)
                        .map { IncrementIntent.Request(it.data) }
                        .flatMap { incrementIntent.execute(it) }
        )
    }

    override fun reduce(
            state: CounterActivityUIState,
            intentResult: Intent.Result?
    ): CounterActivityUIState {
        return when (intentResult) {
            is IncrementIntent.Result -> state.copy(counterValue = intentResult.data)
            else -> state
        }
    }
}
