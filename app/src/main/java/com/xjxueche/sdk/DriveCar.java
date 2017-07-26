package com.xjxueche.sdk;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by lyb on 2017/6/8.
 */

public class DriveCar {
        private Subject<CarSignalInfo> mCarSignalEvent;

        public DriveCar()
        {
                mCarSignalEvent = PublishSubject.<CarSignalInfo>create().toSerialized();
        }

        public void ReadInfo(String data)
        {

            mCarSignalEvent.onNext(processCarData(data));


//            mCarSignalEvent.map(new Function<String,CarSignalInfo>() {
//                        @Override
//                        public CarSignalInfo apply(@NonNull String data) throws Exception {
//
//                                return processCarData(data);
//                        }
//                });

        }

        private CarSignalInfo processCarData(String data)
        {
                CarSignalInfo carSignalInfo = new CarSignalInfo();
                String[] signals = data.split(",");
                carSignalInfo.setLiHe(signals[0].equals("1"));
                carSignalInfo.setShaChe(signals[1].equals("1"));
                carSignalInfo.setSuDu(Double.parseDouble(signals[2]));
                return carSignalInfo;
        }

        public Observable<CarSignalInfo> getSignalChanged()
        {
//            return mObservable.ofType(Object.class);
//            return mCarSignalEvent.ofType(Object.class);
                return mCarSignalEvent.ofType(CarSignalInfo.class);
        }

}
