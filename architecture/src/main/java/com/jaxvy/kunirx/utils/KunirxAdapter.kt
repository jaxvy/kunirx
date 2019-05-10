package com.jaxvy.kunirx.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jaxvy.kunirx.UIAction
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

// Sample RecyclerView.Adapter auto handle Rx subscriptions
abstract class KunirxAdapter<T : KunirxViewHolder> : RecyclerView.Adapter<T>() {

    private val disposableMap = mutableMapOf<T, Disposable?>()
    private val uiActionInputSubject = PublishSubject.create<UIAction.Input>()

    fun uiActionInputObservable(): Observable<UIAction.Input> = uiActionInputSubject

    override fun onViewAttachedToWindow(holder: T) {
        super.onViewAttachedToWindow(holder)
        disposableMap[holder]?.dispose()
        disposableMap[holder] = holder.uiEvents().subscribe {
            uiActionInputSubject.onNext(it)
        }
    }

    override fun onViewDetachedFromWindow(holder: T) {
        super.onViewDetachedFromWindow(holder)
        disposableMap[holder]?.dispose()
    }
}

// Sample RecyclerView.ViewHolder to provide an interface for view holders to trigger Inputs.
abstract class KunirxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun uiEvents(): Observable<UIAction.Input>
}
