package com.xjxueche.utils

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by 刘乙镔 on 2017/7/26.
 */
class PublishScheduler<T>() {

    val dataSource : Subject<T> = PublishSubject.create<T>().toSerialized()
    val workScheduler : Scheduler = Schedulers.newThread()
    val signleExecutor : ExecutorService = Executors.newSingleThreadExecutor()
    val DataArrived : Observable<T>
    get() = dataSource.observeOn(Schedulers.from (signleExecutor))

    fun Add(data:T){
        dataSource.onNext(data)

    }



}