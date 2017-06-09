package com.xjxueche.utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.SerializedObserver;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by 刘乙镔 on 2017/6/9.
 */

public class MoreReactiveList<T> {

    public enum ChangeType {
        ADD, REMOVE
    };

    final List<T> list = new ArrayList<>();

    final Subject<ChangeType> changes;  // (1)变化类型
    final Subject<T> changeValues;      //变化值
    final Observer<T> addObserver;                  // (2)
    final Observer<T> removeObserver;

    public Observable<ChangeType> changes() {
        return changes;
    }

    public Observable<T> changeValues() {
        return changeValues;
    }

    public Observable<T> list() {                   // (3)
        List<T> copy = new ArrayList<>();
        synchronized (list) {
            copy.addAll(list);
        }
        return Observable.fromIterable(copy);
    }

    public Observer<T> adder() {                    // (4)
        return addObserver;
    }

    public Observer<T> remover() {
        return removeObserver;
    }

    void onAdd(T value) {                           // (5)
        synchronized (list) {
            list.add(value);
        }
        changes.onNext(ChangeType.ADD);
        changeValues.onNext(value);
    }

    void onRemove(T value) {
        synchronized (list) {
            if (!list.remove(value)) {
                return;
            }
        }
        changes.onNext(ChangeType.REMOVE);
        changeValues.onNext(value);
    }

    void clear() {
        synchronized (list) {
            list.clear();
        }
    }

    public MoreReactiveList() {
        changes =
                PublishSubject.<ChangeType>create()
                        .toSerialized();                     // (1)

        changeValues =
                BehaviorSubject.<T>create()
                        .toSerialized();

        addObserver = new SerializedObserver<>(         // (2)
                new Observer<T>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull T t) {
                        onAdd(t);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        clear();
                        changes.onError(e);
                        changeValues.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                }
        );
        removeObserver = new SerializedObserver<>(      // (3)
                new Observer<T>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull T t) {
                        onRemove(t);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        clear();
                        changes.onError(e);
                        changeValues.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        clear();
                        changes.onComplete();
                        changeValues.onComplete();
                    }
                }
        );
//        addObserver = new SerializedObserver<>(      // (2)
//                Observers.create(
//                        this::onAdd,
//                        t -> {
//                            clear();
//                            changes.onError(t);
//                            changeValues.onError(t);
//                        },
//                        () -> {
//                            clear();
//                            changes.onCompleted();
//                            changeValues.onCompleted();
//                        }
//                ));
//        removeObserver = new SerializedObserver<>(   // (3)
//                Observers.create(
//                        this::onRemove,
//                        t -> {
//                            clear();
//                            changes.onError(t);
//                            changeValues.onError(t);
//                        },
//                        () -> {
//                            clear();
//                            changes.onCompleted();
//                            changeValues.onCompleted();
//                        }
//                ));
    }
}
