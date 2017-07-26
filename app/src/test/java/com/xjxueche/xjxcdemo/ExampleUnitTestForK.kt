package com.xjxueche.xjxcdemo

import com.xjxueche.sdk.DriveCar3
import com.xjxueche.utils.PublishScheduler
import io.reactivex.Scheduler
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
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

//    @Test
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

    @Test
    @Throws(Exception::class)
    fun PublishSchedulerTest(){
        val publishScheduler = PublishScheduler<String>()
//        publishScheduler.DataArrived.subscribeWith<String>{data ->
//            System.out.println(Thread.currentThread())
//            System.out.println(data)
//        }
        val disposable = publishScheduler.DataArrived.subscribe{ data ->
            System.out.println(Thread.currentThread())
            System.out.println(data)
        }
        val datas = listOf<String>("","","","","","")
        var i : Int = 0
        while (i < 50){
            publishScheduler.Add(i.toString())
            System.out.println(Thread.currentThread().toString() + " send"+ i)
            i++
            Thread.sleep(200)
        }

        disposable.dispose()
    }

}