package cain.tencent.com.androidexercisedemo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.AsyncLayoutInflater;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cain.tencent.com.androidexercisedemo.databinding.ActivityRxJavaBinding;
import cain.tencent.com.androidexercisedemo.domain.Address;
import cain.tencent.com.androidexercisedemo.domain.User;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;

public class RxJavaActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityRxJavaBinding databing;
    public static final String TAG = "RxJavaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        databing = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_rx_java, null, false);
//        setContentView(databing.getRoot());
//        databing.btnRxjava.setOnClickListener(this);

        new AsyncLayoutInflater(this).inflate(R.layout.activity_rx_java, null, new AsyncLayoutInflater.OnInflateFinishedListener() {
            @Override
            public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {
                Log.i(TAG, "current thread: " + Thread.currentThread().getName());
                databing = DataBindingUtil.bind(view);
                setContentView(databing.getRoot());
                databing.btnRxjava.setOnClickListener(RxJavaActivity.this);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rxjava:
                rxjavaAction();
                break;
        }
    }

    private void rxjavaAction() {
//        threadSwitch();
//        switchOpration();
//        groupAction();
//        firstOrLastAction();
//        takeAction();
//        skipAction();
//        elementAction();
//        distinctAction();
        debounceAction();
    }

    private void debounceAction() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                if (emitter.isDisposed()) return;
                for (int i = 1; i <= 10; i++) {
                    emitter.onNext(i);
                    Thread.sleep(i * 100);
                }
            }
        }).debounce(500, TimeUnit.MILLISECONDS).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "integer: " + integer);
            }
        });
    }

    private void distinctAction() {
        Observable.just(1, 2, 1, 2, 3, 4, 5, 5, 6).distinct().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "---distinct---integer: " + integer);
            }
        });

        Observable.just(1, 2, 1, 2, 3, 4, 5, 5, 6).distinctUntilChanged().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "---distinctUntilChanged---integer: " + integer);
            }
        });
    }

    private void elementAction() {
        Observable.just(1, 2, 3, 4).elementAtOrError(6).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "integer:" + integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.i(TAG, "throwable: " + throwable.getMessage());
            }
        });

        Observable.just(1, 2, 3, 4).ignoreElements().subscribe(new Action() {
            @Override
            public void run() throws Exception {
                Log.i(TAG, "---run---");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.i(TAG, "throwable: " + throwable.getMessage());
            }
        });
    }

    private void skipAction() {
        Observable.just(1, 2, 3, 4).skip(6).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "integer:" + integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.i(TAG, "throwable: " + throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                Log.i(TAG, "---run---");
            }
        });
    }

    private void takeAction() {
        Observable.just(1, 2, 3, 4).take(6).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "integer:" + integer);
            }
        });

        Observable.just(1, 2, 3, 4).takeLast(2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "integer:" + integer);
            }
        });
    }

    private void firstOrLastAction() {
        Observable.range(1, 5).firstOrError().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "integer: " + integer);
            }
        });

        Observable.range(5, 10).lastOrError().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "integer: " + integer);
            }
        });
    }

    private void groupAction() {
        Observable.range(1, 8).groupBy(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return (integer % 2 == 0) ? "偶数" : "奇数";
            }
        }).subscribe(new Consumer<GroupedObservable<String, Integer>>() {
            @Override
            public void accept(final GroupedObservable<String, Integer> stringIntegerGroupedObservable) throws Exception {
                if (stringIntegerGroupedObservable.getKey().equals("奇数")) {
                    stringIntegerGroupedObservable.subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            Log.i(TAG, stringIntegerGroupedObservable.getKey() + " member: " + integer);
                        }
                    });
                }
            }
        });

        Observable.range(1, 9).buffer(3, 1).subscribe(new Consumer<List<Integer>>() {
            @Override
            public void accept(List<Integer> integers) throws Exception {
                Log.i(TAG, "integers: " + integers.toString());
            }
        });

        Observable.range(1, 9).window(2).subscribe(new Consumer<Observable<Integer>>() {
            @Override
            public void accept(Observable<Integer> integerObservable) throws Exception {
                Log.i(TAG, "---window---");
                integerObservable.subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "integer: " + integer);
                    }
                });
            }
        });


    }

    private void switchOpration() {
        Address address1 = new Address("ren ming road", "ShenZhen");
        Address address2 = new Address("bai yun road", "GuangZhou");
        List<Address> addressList = new ArrayList<>();
        addressList.add(address1);
        addressList.add(address2);
        User user = new User("tony", addressList);

        Observable.just(user).map(new Function<User, List<Address>>() {
            @Override
            public List<Address> apply(User user) throws Exception {
                return user.getAddressList();
            }
        }).subscribe(new Consumer<List<Address>>() {
            @Override
            public void accept(List<Address> addresses) throws Exception {
                for (Address address : addresses) {
                    Log.i(TAG, "---map---address: " + address);
                }
            }
        });

        Observable.just(user).concatMap(new Function<User, ObservableSource<Address>>() {
            @Override
            public ObservableSource<Address> apply(User user) throws Exception {
                return Observable.fromIterable(user.getAddressList());
            }
        }).subscribe(new Consumer<Address>() {
            @Override
            public void accept(Address address) throws Exception {
                Log.i(TAG, "---flatmap---address: " + address);
            }
        });


    }

    private void threadSwitch() {
        Observable.just("Hello World").map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                Log.i(TAG, "---map1 opration---current thread: " + Thread.currentThread().getName());
                return s + " RxJava";
            }
        }).subscribeOn(Schedulers.single()).map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                Log.i(TAG, "---map2 opration---current thread: " + Thread.currentThread().getName());
                return s.toLowerCase();
            }
        }).observeOn(Schedulers.io()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i(TAG, "s = " + s + ", currentThread: " + Thread.currentThread().getName());
            }
        });
    }

}
