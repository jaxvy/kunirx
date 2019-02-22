package me.jaxvy.kunirx.counter

import dagger.Reusable
import io.reactivex.Observable
import me.jaxvy.kunirx.architecture.UIAction
import javax.inject.Inject

@Reusable
class CounterClickUIAction @Inject constructor() :
        UIAction<CounterActivityUIState, CounterClickUIAction.Data> {

    class Data(val currentValue: Int) : UIAction.UIActionData

    override fun execute(data: Data): Observable<UIAction.UIStateMutator> {
        return Observable
                .just<UIAction.UIStateMutator>(
                        CounterClickMutator.Success(data.currentValue + 1)
                )
                .startWith(CounterClickMutator.Loading)
                .onErrorReturn { throwable -> CounterClickMutator.Error(throwable) }
    }

    sealed class CounterClickMutator : UIAction.UIStateMutator {
        object Loading : CounterClickMutator()
        class Error(val throwable: Throwable) : CounterClickMutator()
        class Success(val newValue: Int) : CounterClickMutator()
    }

    override fun reduce(
            oldState: CounterActivityUIState,
            uiStateMutator: UIAction.UIStateMutator
    ): CounterActivityUIState {
        return when (uiStateMutator) {
            is CounterClickMutator.Loading -> oldState
            is CounterClickMutator.Success -> oldState.copy(counterValue = uiStateMutator.newValue)
            // The error can be propagated to the UIState for show providing feedback to the users.
            is CounterClickMutator.Error -> oldState
            else -> oldState
        }
    }
}
