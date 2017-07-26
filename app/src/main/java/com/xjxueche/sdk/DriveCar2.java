package com.xjxueche.sdk;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by 刘乙镔 on 2017/6/9.
 */

public class DriveCar2 {
    public enum SignalItem{
        LiHe,ShaChe,SuDu
    }
    private boolean mLiHe;
    private boolean mShaChe;
    private double mSuDu;

    public boolean isLiHe() {
        return mLiHe;
    }

    public boolean isShaChe() {
        return mShaChe;
    }

    public double getSuDu() {
        return mSuDu;
    }

    final Subject<SignalItem> changedItem;
    final Subject changeValues;      //变化值

    public DriveCar2()
    {
        changedItem =
                PublishSubject.<SignalItem>create()
                        .toSerialized();                     // (1)

        changeValues =
                BehaviorSubject.create()
                        .toSerialized();
    }

    public void onReadInfo(CarSignalInfo data)
    {
        if(data.isLiHe() != mLiHe){
            mLiHe = data.isLiHe();
            changedItem.onNext(SignalItem.LiHe);
        }
        if(data.isShaChe() != mShaChe){
            mShaChe = data.isShaChe();
            changedItem.onNext(SignalItem.ShaChe);
        }
        if(data.getSuDu() != mSuDu){
            mSuDu = data.getSuDu();
            changedItem.onNext(SignalItem.SuDu);
        }
    }


}
