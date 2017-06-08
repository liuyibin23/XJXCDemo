package com.xjxueche.xjxcdemo;

import com.xjxueche.sdk.CarSignInfo;
import com.xjxueche.sdk.DriveCar;
import com.xjxueche.sdk.RxBus;

import org.junit.Test;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public  void DriveCarTest() throws Exception{
        DriveCar car = new DriveCar();
        car.toObserverable().subscribe(new Consumer<CarSignInfo>() {
            @Override
            public void accept(@NonNull CarSignInfo carSignInfo) throws Exception {
                assertEquals(true,carSignInfo.isLiHe());
                assertEquals(false,carSignInfo.isShaChe());
                assertEquals(20.0,carSignInfo.getSuDu(),0);
            }
        });
        String data = "1,0,10.0";
        car.ReadInfo(data);

    }

    @Test
    public void RxBusTest()throws Exception{
//        RxBus bus = new RxBus();
        RxBus.getInstance().post("111");
        RxBus.getInstance().toObserverable(String.class).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                assertEquals("111",s);
            }
        });
    }

}