package com.jaxvy.kunirx.counter.ui

import com.jaxvy.kunirx.UIAction
import com.jaxvy.kunirx.counter.ui.DecrementClickUIAction.CounterClickMutator
import com.jaxvy.kunirx.counter.ui.DecrementClickUIAction.Input
import dagger.Reusable
import io.reactivex.Observable
import javax.inject.Inject

@Reusable
class DecrementClickUIAction @Inject constructor() :
        UIAction<CounterActivityUIState, Input, CounterClickMutator> {

    class Input(val currentValue: Int) : UIAction.Input

    override fun execute(input: Input): Observable<CounterClickMutator> {
        return Observable
                .just<CounterClickMutator>(CounterClickMutator.Success(input.currentValue - 1))
                .startWith(CounterClickMutator.Loading)
                .onErrorReturn { throwable -> CounterClickMutator.Error(throwable) }
    }

    sealed class CounterClickMutator : UIAction.UIStateMutator {
        object Loading : CounterClickMutator()
        class Error(val throwable: Throwable) : CounterClickMutator()
        class Success(val newValue: Int) : CounterClickMutator()
    }

    override fun reduce(
            uiState: CounterActivityUIState,
            uiStateMutator: CounterClickMutator
    ): CounterActivityUIState {
        return when (uiStateMutator) {
            is CounterClickMutator.Loading -> uiState
            is CounterClickMutator.Success -> uiState.copy(counterValue = uiStateMutator.newValue)
            // The error can be propagated to the UIState for show providing feedback to the users.
            is CounterClickMutator.Error -> uiState
        }
    }
}
