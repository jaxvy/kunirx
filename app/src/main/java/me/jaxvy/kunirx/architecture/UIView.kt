package me.jaxvy.kunirx.architecture

import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable

interface UIEvent

interface UIState : Parcelable

abstract class UIView<U : UIState> : AppCompatActivity() {

    open lateinit var uiState: U

    abstract val renderer: Renderer<U>

    abstract val viewTag: String

    abstract fun uiActionEvents(): Observable<UIEvent>

    abstract fun render(uiState: U)

    abstract fun initializeUIState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.run {
            getParcelable<U>(viewTag)?.let { uiState = it }
        } ?: initializeUIState()
    }

    override fun onResume() {
        super.onResume()
        renderer.start(this)
        render(uiState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(viewTag, uiState)
    }

    override fun onStop() {
        super.onStop()
        renderer.stop()
    }
}
