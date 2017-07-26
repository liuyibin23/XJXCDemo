package com.xjxueche.utils;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 刘乙镔 on 2017/7/26.
 */

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 *
 * By convention each UseCase implementation will return the result using a {@link DisposableObserver}
 * that will execute its job in a background thread and will post the result in the UI thread.
 */
public abstract class UseCase<T, Params> {

    protected final Scheduler subscribeScheduler;
    protected final Scheduler observeScheduler;
    private final CompositeDisposable disposables;

    protected UseCase(Scheduler subscribeScheduler, Scheduler observeScheduler) {
        this.subscribeScheduler = subscribeScheduler;
        this.observeScheduler = observeScheduler;
        this.disposables = new CompositeDisposable();
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    abstract Observable<T> buildUseCaseObservable(Params params);

    /**
     * Executes the current use case.
     *
     * @param observer {@link DisposableObserver} which will be listening to the observable build
     * by {@link #buildUseCaseObservable(Params)} ()} method.
     * @param params Parameters (Optional) used to build/execute this use case.
     */
    public void execute(DisposableObserver<T> observer, Params params){
        //Cold observables
//        Preconditions.checkNotNull(observer);
        final Observable<T> observable = this.buildUseCaseObservable(params)
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler);
        addDisposable(observable.subscribeWith(observer));
    }

    public void publish(DisposableObserver<T> observer, Params params){
        //Hot observables
        ConnectableObservable<T> observable = this.buildUseCaseObservable(params)
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler).publish();
        observable.connect();
        addDisposable(observable.subscribeWith(observer));
    }

    /**
     * Dispose from current {@link CompositeDisposable}.
     */
    public void dispose() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }

    /**
     * Dispose from current {@link CompositeDisposable}.
     */
    protected void addDisposable(Disposable disposable) {
//        Preconditions.checkNotNull(disposable);
//        Preconditions.checkNotNull(disposables);
        disposables.add(disposable);
    }
}
