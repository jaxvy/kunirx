package me.jaxvy.kunirx.architecture

import io.reactivex.Observable

interface Intent<in I : Intent.Request, O : Intent.Result> {

    fun execute(input: I): Observable<O>

    interface Request

    interface Result
}

