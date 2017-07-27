package com.xjxueche.sdk

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

/**
 * Created by lyb on 2017/6/8.
 */

class DriveCar3 {

    var LiHe : Boolean = false
    var ShaChe : Boolean = false
    var SuDu : Double = 0.0

    private val mCarSignalEvent: Subject<CarSignalInfo2> = PublishSubject.create<CarSignalInfo2>().toSerialized()
    val signalChanged: Observable<CarSignalInfo2>
        get() = mCarSignalEvent.ofType<CarSignalInfo2>(CarSignalInfo2::class.java)

    fun ReadInfo(data: String) {

//        mCarSignalEvent.onNext(processCarData(data))

        val carSignalInfo = CarSignalInfo2()
        val signals = data.split(",")
        carSignalInfo.LiHeChanged = LiHe != (signals[0] == "1")
        if(carSignalInfo.LiHeChanged){
            LiHe = signals[0] == "1"
        }
        carSignalInfo.ShaCheChanged = ShaChe != (signals[1] == "1")
        if(carSignalInfo.ShaCheChanged){
            ShaChe = signals[1] == "1"
        }

        carSignalInfo.SuDuChanged = Math.abs(SuDu - signals[2].toDouble()) > 0.001
        if(carSignalInfo.SuDuChanged){
            SuDu = signals[2].toDouble()
        }
        mCarSignalEvent.onNext(carSignalInfo)
    }

    private fun processCarData(data: String): CarSignalInfo2 {
        val carSignalInfo = CarSignalInfo2()

////        val signals = data.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        val signals = data.split(",")
        carSignalInfo.LiHe = signals[0] == "1"
        carSignalInfo.ShaChe = signals[1] == "1"
        carSignalInfo.SuDu = signals[2].toDouble()
//        carSignalInfo.SuDu = java.lang.Double.parseDouble(signals[2])
        return carSignalInfo
    }


}