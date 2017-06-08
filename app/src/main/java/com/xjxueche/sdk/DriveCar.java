package com.xjxueche.sdk;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by lyb on 2017/6/8.
 */

public class DriveCar {
        private Subject<String> mCarSignalEvent;

        public DriveCar()
        {
                mCarSignalEvent = PublishSubject.create();
        }

        public void ReadInfo(String data)
        {
                mCarSignalEvent.onNext(data);
            mCarSignalEvent.just("1").subscribe(new Consumer<String>() {
                @Override
                public void accept(@NonNull String s) throws Exception {
                    Object o1 = s;
                }
            });
//                mCarSignalEvent.just("1").subscribe(new Consumer() {
//                        @Override
//                        public void accept(@NonNull Object o) throws Exception {
//                                Object o1 = o;
//                        }
//                });


                mCarSignalEvent.map(new Function<String,CarSignInfo>() {
                        @Override
                        public CarSignInfo apply(@NonNull String data) throws Exception {

                                return processCarData(data);
                        }
                });
        }

        private CarSignInfo processCarData(String data)
        {
                CarSignInfo carSignInfo = new CarSignInfo();
                String[] signals = data.split(",");
                carSignInfo.setLiHe(signals[0] == "1");
                carSignInfo.setShaChe(signals[1] == "1");
                carSignInfo.setSuDu(Double.parseDouble(signals[2]));
                return carSignInfo;
        }

        public Observable<CarSignInfo> toObserverable()
        {
                return mCarSignalEvent.ofType(CarSignInfo.class);
        }

}
