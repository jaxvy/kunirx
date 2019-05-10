package com.jaxvy.kunirx.utils

import com.jaxvy.kunirx.UIAction
import com.jaxvy.kunirx.UIState
import io.reactivex.Observable

// Side effect UIAction that only executes some logic and does not update the UIState
abstract class KunirxSideEffectUIAction<U : UIState, in E : UIAction.Input> :
    UIAction<U, E, KunirxSideEffectUIAction.SideEffectMutator> {

    sealed class SideEffectMutator : UIAction.UIStateMutator {
        object Empty : SideEffectMutator()
    }

    override fun execute(input: E): Observable<SideEffectMutator> {
        sideEffect(input)
        return Observable.just(SideEffectMutator.Empty)
    }

    open fun sideEffect(input: E) {}

    override fun reduce(
        uiState: U,
        uiStateMutator: SideEffectMutator
    ): U = uiState
}
