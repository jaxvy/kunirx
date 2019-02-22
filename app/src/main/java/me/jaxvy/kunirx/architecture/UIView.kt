package me.jaxvy.kunirx.architecture

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.Scheduler
import me.jaxvy.kunirx.di.IOScheduler
import me.jaxvy.kunirx.di.MainScheduler
import javax.inject.Inject

interface UIState

interface UIActionListProvider<U : UIState> {
    fun uiActions(): List<UIAction<U, *>>
}

abstract class UIView<U : UIState> : AppCompatActivity() {

    open lateinit var uiState: U

    @field:[Inject MainScheduler]
    lateinit var mainScheduler: Scheduler

    @field:[Inject IOScheduler]
    lateinit var ioScheduler: Scheduler

    private lateinit var renderer: Renderer<U>

    abstract fun uiActionDataObservable(): Observable<UIAction.UIActionData>

    abstract fun uiActionListProvider(): UIActionListProvider<U>

    abstract fun render(uiState: U)

    override fun onResume() {
        super.onResume()
        renderer = Renderer(
                mainScheduler = mainScheduler,
                ioScheduler = ioScheduler
        )
        renderer.start(this)
        render(uiState)
    }

    override fun onStop() {
        super.onStop()
        renderer.stop()
    }
}
