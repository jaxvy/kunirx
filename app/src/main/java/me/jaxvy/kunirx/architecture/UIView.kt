package me.jaxvy.kunirx.architecture

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import io.reactivex.Observable

interface UIState

interface UIView<U : UIState> {

    var uiState: U

    var renderer: Renderer<U>

    fun rendererConfig(): RendererConfig<U>

    fun uiActionDataObservable(): Observable<UIAction.UIActionData>

    fun render(uiState: U)
}

// Base class that Activities can use
abstract class UIViewActivity<U : UIState> : UIView<U>, AppCompatActivity() {

    override lateinit var uiState: U

    override lateinit var renderer: Renderer<U>

    override fun onStart() {
        super.onStart()
        renderer = Renderer(rendererConfig())
        renderer.start(this)

        render(uiState)
    }

    override fun onStop() {
        renderer.stop()
        super.onStop()
    }
}

// Base class that Fragments can use
abstract class UIViewFragment<U : UIState> : UIView<U>, Fragment() {

    override lateinit var uiState: U

    override lateinit var renderer: Renderer<U>

    override fun onStart() {
        super.onStart()
        renderer = Renderer(rendererConfig())
        renderer.start(this)

        render(uiState)
    }

    override fun onStop() {
        renderer.stop()
        super.onStop()
    }
}
