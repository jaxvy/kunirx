package com.jaxvy.kunirx.architecture

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import io.reactivex.Observable

interface UIState

interface UIView<U : UIState> {

    var uiState: U

    var uiActionHandler: UIActionHandler<U>

    fun uiActionHandlerConfiguration(): UIActionHandler.Configuration

    fun uiActionDataObservable(): Observable<UIAction.Input>

    fun render(uiState: U)
}

// Base class that Activities can use
abstract class UIViewActivity<U : UIState> : UIView<U>, AppCompatActivity() {

    override lateinit var uiState: U

    override lateinit var uiActionHandler: UIActionHandler<U>

    override fun onStart() {
        super.onStart()
        uiActionHandler = UIActionHandler(uiActionHandlerConfiguration())
        uiActionHandler.start(this)

        render(uiState)
    }

    override fun onStop() {
        uiActionHandler.stop()
        super.onStop()
    }
}

// Base class that Fragments can use
abstract class UIViewFragment<U : UIState> : UIView<U>, Fragment() {

    override lateinit var uiState: U

    override lateinit var uiActionHandler: UIActionHandler<U>

    override fun onStart() {
        super.onStart()
        uiActionHandler = UIActionHandler(uiActionHandlerConfiguration())
        uiActionHandler.start(this)

        render(uiState)
    }

    override fun onStop() {
        uiActionHandler.stop()
        super.onStop()
    }
}
