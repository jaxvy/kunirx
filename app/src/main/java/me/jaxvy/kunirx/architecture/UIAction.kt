package me.jaxvy.kunirx.architecture

import io.reactivex.Observable

interface UIAction<U : UIState, E : UIEvent> {

    interface UIStateMutator

    val uiEvent: Class<E>

    fun execute(event: E): Observable<UIAction.UIStateMutator>

    fun reduce(oldState: U, uiStateMutator: UIAction.UIStateMutator): U
}
