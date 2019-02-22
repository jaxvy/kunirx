package me.jaxvy.kunirx.architecture

import io.reactivex.Observable

interface UIAction<U : UIState, in E : UIAction.UIActionData> {

    /**
     * UIActionData needs to be defined inside its corresponding UIAction. This is necessary for
     * Renderer to match a given UIActionData to its corresponding UIAction.
     */
    interface UIActionData

    interface UIStateMutator

    fun execute(data: E): Observable<UIAction.UIStateMutator>

    fun reduce(oldState: U, uiStateMutator: UIAction.UIStateMutator): U
}
