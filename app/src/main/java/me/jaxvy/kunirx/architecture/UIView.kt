package me.jaxvy.kunirx.architecture

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable

interface UIState

abstract class UIView<U : UIState> : AppCompatActivity() {

    open lateinit var uiState: U

    abstract val renderer: Renderer<U>

    abstract fun uiActionDataObservable(): Observable<UIAction.UIActionData>

    abstract fun render(uiState: U)

    override fun onResume() {
        super.onResume()
        renderer.start(this)
        render(uiState)
    }

    override fun onStop() {
        super.onStop()
        renderer.stop()
    }
}
