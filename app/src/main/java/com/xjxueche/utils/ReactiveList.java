package com.xjxueche.utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

/**
 * Created by 刘乙镔 on 2017/6/9.
 */

public class ReactiveList<T>  {
    public enum ChangeType {
        ADD, REMOVE, UPDATE                      // (1)
    };

    final List<T> list = new ArrayList<>();      // (2)

    final PublishSubject<ChangeType> changes =
            PublishSubject.create();             // (3)
    final BehaviorSubject<T> changeValues =
            BehaviorSubject.create();
    final ReplaySubject<T> latestAdded =
            ReplaySubject.createWithSize(10);

    public Observable<ChangeType> changes() {    // (4)
        return changes;
    }

    public Observable<T> changeValues() {
        return changeValues;
    }

    public Observable<T> latestAdded() {
        return latestAdded;
    }

    public void add(T value) {
        list.add(value);
        changes.onNext(ChangeType.ADD);
        changeValues.onNext(value);
        latestAdded.onNext(value);
    }
    public void remove(T value) {
        if (list.remove(value)) {
            changes.onNext(ChangeType.REMOVE);
            changeValues.onNext(value);
        }
    }
    public void replace(T value, T newValue) {
        int index = list.indexOf(value);
        if (index >= 0) {
            list.set(index, newValue);
            changes.onNext(ChangeType.UPDATE);
            changeValues.onNext(newValue);
        }
    }
}
