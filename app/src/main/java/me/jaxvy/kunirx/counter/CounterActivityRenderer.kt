package me.jaxvy.kunirx.counter

import dagger.Reusable
import me.jaxvy.kunirx.architecture.Renderer
import me.jaxvy.kunirx.architecture.UIAction
import me.jaxvy.kunirx.architecture.UIEvent
import javax.inject.Inject

@Reusable
class CounterActivityRenderer @Inject constructor(
        private val counterClickUIAction: CounterClickUIAction
) : Renderer<CounterActivityUIState>() {

    override fun uiActions(): List<UIAction<CounterActivityUIState, UIEvent>> {
        return listOfNotNull(
                counterClickUIAction as? UIAction<CounterActivityUIState, UIEvent>
        )
    }
}
