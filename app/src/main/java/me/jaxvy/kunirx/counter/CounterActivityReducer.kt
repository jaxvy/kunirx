package me.jaxvy.kunirx.counter

import dagger.Reusable
import me.jaxvy.kunirx.architecture.Intent
import me.jaxvy.kunirx.architecture.Reducer
import javax.inject.Inject

@Reusable
class CounterActivityReducer @Inject constructor() : Reducer<CounterActivityUIState> {
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
