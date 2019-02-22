package me.jaxvy.kunirx.counter

import dagger.Reusable
import io.reactivex.Observable
import me.jaxvy.kunirx.architecture.Intent
import javax.inject.Inject

@Reusable
class IncrementIntent @Inject constructor() :
        Intent<IncrementIntent.Request, IncrementIntent.Result> {

    class Request(val data: Int) : Intent.Request

    class Result(val data: Int) : Intent.Result

    override fun execute(input: Request): Observable<Result> {
        return Observable.just(Result(input.data + 1))
    }
}
