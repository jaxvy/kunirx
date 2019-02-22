package me.jaxvy.kunirx.architecture

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable

interface UIState

abstract class UIViewActivity<U : UIState> : AppCompatActivity() {

    open lateinit var uiState: U

    private lateinit var renderer: Renderer<U>

    abstract fun uiActionDataObservable(): Observable<UIAction.UIActionData>

    abstract fun rendererConfig(): RendererConfig<U>

    abstract fun render(uiState: U)

    override fun onResume() {
        super.onResume()
        renderer = Renderer(rendererConfig())
        renderer.start(this)

        render(uiState)
    }

    override fun onStop() {
        super.onStop()
        renderer.stop()
    }
}
