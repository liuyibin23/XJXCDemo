package com.xjxueche.xjxcdemo;

import com.xjxueche.sdk.CarSignalInfo;
import com.xjxueche.sdk.DriveCar;
import com.xjxueche.sdk.RxBus;
import com.xjxueche.utils.MoreReactiveList;
import com.xjxueche.utils.ReactiveList;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

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
        car.getSignalChanged().subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                CarSignalInfo carSignalInfo = (CarSignalInfo)o;
                assertEquals(true, carSignalInfo.isLiHe());
                assertEquals(false, carSignalInfo.isShaChe());
                assertEquals(10.0, carSignalInfo.getSuDu(),0);
            }
        });
        String data = "1,0,10.0";
        car.ReadInfo(data);
//        CompositeDisposable
    }

    //region RxBusTest
    @Test
    public void RxBusTest()throws Exception{
//        RxBus bus = new RxBus();
        RxBus.getInstance().toObserverable(String.class).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                assertEquals("111",s);
            }
        });
        RxBus.getInstance().post("111");
    }
    //endregion

    @Test
    public void ReactiveListTest(){
        final ReactiveList<Long> list = new ReactiveList<>();



//        Observable.range(0,10)
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(@NonNull Long aLong) throws Exception {
//                        list.add(aLong);
//                        System.out.println("add " + aLong);
//                    }
//                });
//        Observable.timer(4,TimeUnit.SECONDS)
//                .take(10)
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(@NonNull Long aLong) throws Exception {
//                        list.remove(aLong);
//                        System.out.println("remove " + aLong);
//                    }
//                });
        list.changes().subscribe(new Consumer<ReactiveList.ChangeType>() {
            @Override
            public void accept(@NonNull ReactiveList.ChangeType changeType) throws Exception {
                System.out.println(changeType);
            }
        });
//        list.changeValues().toBlocking().forEach(System.out::println);
        list.changeValues().subscribe(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long aLong) throws Exception {
                System.out.println(aLong);
            }
        });

        Observable.range(0,10)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        list.add( integer.longValue());
                        System.out.println("add " + integer);
                    }
                });
        Observable.range(0,10)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        list.remove(integer.longValue());
                        System.out.println("remove " + integer);
                    }
                });
    }

    @Test
    public void MoreReactiveListTest(){
        final MoreReactiveList<Long> list = new MoreReactiveList<>();


        list.changes().subscribe(new Consumer<MoreReactiveList.ChangeType>() {
            @Override
            public void accept(@NonNull MoreReactiveList.ChangeType changeType) throws Exception {
                System.out.println("变化类型 "+changeType);
            }
        });

        list.changes()
                .flatMap(new Function<MoreReactiveList.ChangeType, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull MoreReactiveList.ChangeType changeType) throws Exception {
                        return list.list();
                    }
                }).subscribe(new Consumer<Object>() {
                @Override
                public void accept(@NonNull Object value) throws Exception {
                    System.out.println("当前List中的值：" + value);
                }
        });
        list.changeValues().forEach(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long aLong) throws Exception {
                System.out.println("变化值 "+aLong);
            }
        });

        Observable.range(0,10)
                .map(new Function<Integer, Long>() {
                    @Override
                    public Long apply(@NonNull Integer integer) throws Exception {
                        return integer.longValue();
                    }
                })
                .subscribe(list.adder());
//        Observable.range(0,10)
//                .subscribe(new Consumer<Integer>() {
//                    @Override
//                    public void accept(@NonNull Integer integer) throws Exception {
//                        list.remove(integer.longValue());
//                        System.out.println("remove " + integer);
//                    }
//                });
//        Observable.interval(0, 1, TimeUnit.SECONDS)
//                .take(10)
//                .subscribe(list.adder());
//
//        Observable.interval(4, 1, TimeUnit.SECONDS)
//                .take(10)
//                .subscribe(list.remover());


//        list.changes().subscribe(System.out::println);

//        list.changes()
//                .flatMap(e -> list.list().toList())
//                .subscribe(System.out::println);
//
//        list.changeValues.toBlocking().forEach(System.out::println);
    }
}