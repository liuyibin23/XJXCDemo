package com.xjxueche.xjxcdemo

import com.xjxueche.sdk.DriveCar3
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Consumer
import org.junit.Test
import org.junit.Assert.*
/**
 * Created by 刘乙镔 on 2017/7/25.
 */
class ExampleUnitTestForK {
//    @Test
    @Throws(Exception::class)
    fun DriveCarTest() {
        val car = DriveCar3()
        car.signalChanged.subscribe { o ->
            val carSignalInfo = o
            assertEquals(true, carSignalInfo.LiHe)
            assertEquals(false, carSignalInfo.ShaChe)
            assertEquals(10.0, carSignalInfo.SuDu, 0.0)
        }
        val data = "1,0,10.0"
        car.ReadInfo(data)
        //        CompositeDisposable
    }

    @Test
    @Throws(Exception::class)
    fun DriveCarTest2(){
        val car = DriveCar3()
        car.signalChanged.subscribe { o ->
            val carSignalInfo = o
            assertEquals(true, car.LiHe)
            assertEquals(false, car.ShaChe)
            assertEquals(10.0, car.SuDu, 0.0)
        }
        val data = "1,0,10.0"
        car.ReadInfo(data)
    }

}