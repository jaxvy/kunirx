package com.jaxvy.kunirx

import io.reactivex.Observable

interface UIAction<U : UIState, in E : UIAction.Input, M : UIAction.UIStateMutator> {


    // Input needs to be defined inside its corresponding UIAction. This is necessary for
    // UIActionHandler to match a given Input to its corresponding UIAction.
    interface Input


    // UIStateMutator is the interface for the data that you want to pass from execute() to
    // reduce()
    interface UIStateMutator

    fun execute(input: E): Observable<M>

    fun reduce(uiState: U, uiStateMutator: M): U
}
