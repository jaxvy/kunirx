package me.jaxvy.kunirx.architecture

import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable

interface UIEvent

interface UIState : Parcelable

abstract class UIView<U : UIState> : AppCompatActivity() {

    private val UI_STATE_BUNDLE_KEY = this::class.java.name

    open lateinit var uiState: U

    abstract val renderer: Renderer<U>

    abstract fun uiEvents(): Observable<UIEvent>

    abstract fun render(uiState: U)

    abstract fun initializeUIState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.run {
            getParcelable<U>(UI_STATE_BUNDLE_KEY)?.let { uiState = it }
        } ?: initializeUIState()
    }

    override fun onResume() {
        super.onResume()
        renderer.start(this)
        render(uiState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(UI_STATE_BUNDLE_KEY, uiState)
    }

    override fun onStop() {
        super.onStop()
        renderer.stop()
    }
}
