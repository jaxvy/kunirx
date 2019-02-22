package me.jaxvy.kunirx.counter

import dagger.Reusable
import io.reactivex.Observable
import me.jaxvy.kunirx.architecture.Intent
import me.jaxvy.kunirx.architecture.UIEvent
import me.jaxvy.kunirx.architecture.UIEventToIntentMapper
import javax.inject.Inject

@Reusable
class CounterActivityUIEventToIntentMapper @Inject constructor(
        private val incrementIntent: IncrementIntent
) : UIEventToIntentMapper {

    override fun map(uiEvent: Observable<UIEvent>): Observable<Intent.Result> {
        return Observable.mergeArray(
                uiEvent.ofType(CounterClickUIEvent::class.java)
                        .map { IncrementIntent.Request(it.data) }
                        .flatMap { incrementIntent.execute(it) }
        )
    }
}
