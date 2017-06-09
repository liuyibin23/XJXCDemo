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
        private Subject<CarSignInfo> mCarSignalEvent;
        private Observable mObservable;

        public DriveCar()
        {
                mCarSignalEvent = PublishSubject.<CarSignInfo>create().toSerialized();
        }

        public void ReadInfo(String data)
        {

            mCarSignalEvent.onNext(processCarData(data));


//            mCarSignalEvent.map(new Function<String,CarSignInfo>() {
//                        @Override
//                        public CarSignInfo apply(@NonNull String data) throws Exception {
//
//                                return processCarData(data);
//                        }
//                });

        }

        private CarSignInfo processCarData(String data)
        {
                CarSignInfo carSignInfo = new CarSignInfo();
                String[] signals = data.split(",");
                carSignInfo.setLiHe(signals[0].equals("1"));
                carSignInfo.setShaChe(signals[1].equals("1"));
                carSignInfo.setSuDu(Double.parseDouble(signals[2]));
                return carSignInfo;
        }

        public Observable<CarSignInfo> toObserverable()
        {
//            return mObservable.ofType(Object.class);
//            return mCarSignalEvent.ofType(Object.class);
                return mCarSignalEvent.ofType(CarSignInfo.class);
        }

}
