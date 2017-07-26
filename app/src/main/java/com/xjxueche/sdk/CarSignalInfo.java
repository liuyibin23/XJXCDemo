package com.xjxueche.sdk;

/**
 * Created by 刘乙镔 on 2017/6/8.
 */

public class CarSignalInfo {
    private boolean mLiHeChanged;
    private boolean mShaCheChanged;
    private boolean mSuDuChanged;

    private boolean mLiHe;
    private boolean mShaChe;
    private double mSuDu;

    public boolean isLiHeChanged() {
        return mLiHeChanged;
    }

    public void setLiHeChanged(boolean liHeChanged) {
        mLiHeChanged = liHeChanged;
    }

    public boolean isShaCheChanged() {
        return mShaCheChanged;
    }

    public void setShaCheChanged(boolean shaCheChanged) {
        mShaCheChanged = shaCheChanged;
    }

    public boolean isSuDuChanged() {
        return mSuDuChanged;
    }

    public void setSuDuChanged(boolean suDuChanged) {
        mSuDuChanged = suDuChanged;
    }

    public boolean isLiHe() {
        return mLiHe;
    }

    public void setLiHe(boolean liHe) {
        mLiHe = liHe;
    }

    public boolean isShaChe() {
        return mShaChe;
    }

    public void setShaChe(boolean shaChe) {
        mShaChe = shaChe;
    }

    public double getSuDu() {
        return mSuDu;
    }

    public void setSuDu(double suDu) {
        mSuDu = suDu;
    }
}
