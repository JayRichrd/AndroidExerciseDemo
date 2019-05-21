package cain.tencent.com.androidexercisedemo;

import android.annotation.SuppressLint;
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
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("CheckResult")
public class RxJavaActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityRxJavaBinding databing;
    public static final String TAG = "RxJavaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        databing = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout
//        .activity_rx_java, null, false);
//        setContentView(databing.getRoot());
//        databing.btnRxjava.setOnClickListener(this);

        new AsyncLayoutInflater(this).inflate(R.layout.activity_rx_java, null,
                new AsyncLayoutInflater.OnInflateFinishedListener() {
                    @Override
                    public void onInflateFinished(@NonNull View view, int resid,
                                                  @Nullable ViewGroup parent) {
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
//        debounceAction();
//        logicalAction();
        combineAction();
    }

    private void combineAction() {
        Observable odds = Observable.just(1, 3, 5);
        Observable evens = Observable.just(2, 4, 6, 8);
        Observable.mergeDelayError(odds, evens).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "---merge---integer: " + integer);
            }
        });

        Observable.zip(odds, evens, new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer o, Integer o2) throws Exception {
                return o + o2;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "---zip---integer: " + integer);
            }
        });

        Observable.combineLatest(odds, evens, new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer o, Integer o2) throws Exception {
                Log.i(TAG, "---combineLatest---o:" + o + ", o2: " + o2);
                return o + o2;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "---combineLatest---integer: " + integer);
            }
        });

        Observable<Integer> o1 = Observable.just(1, 2, 3);
        Observable<Integer> o2 = Observable.just(4, 5, 6);
        o1.join(o2, new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                return Observable.just(String.valueOf(integer)).delay(100, TimeUnit.MILLISECONDS);
            }
        }, new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                return Observable.just(String.valueOf(integer)).delay(200, TimeUnit.MILLISECONDS);
            }
        }, new BiFunction<Integer, Integer, String>() {
            @Override
            public String apply(Integer integer, Integer integer2) throws Exception {
                return integer + ":" + integer2;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i(TAG, "---join---s: " + s);
            }
        });

        Observable.just("Hello Java", "Hello C++").startWith("Hello Rxjava").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i(TAG, "---startWith---s: " + s);
            }
        });

    }

    private void logicalAction() {
        Observable.just(1, 2, 3, 4, 5).all(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer < 10;
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                Log.i(TAG, "---all---aBoolean: " + aBoolean);
            }
        });

        Observable.just(1, 2, 3, 4, 5).contains(3).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                Log.i(TAG, "---contains---aBoolean: " + aBoolean);
            }
        });

        Observable.ambArray(Observable.just(1, 2, 3).delay(1, TimeUnit.SECONDS),
                Observable.just(4, 5, 6)).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "---amb---integer: " + integer);
            }
        });

        Observable.empty().defaultIfEmpty(9).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                Log.i(TAG, "---defaultIfEmpty---o: " + o);
            }
        });

        Observable.empty().switchIfEmpty(Observable.just(1, 2, 3)).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                Log.i(TAG, "---switchIfEmpty---o: " + o);
            }
        });

        Observable.sequenceEqual(Observable.just(1, 2, 3),
                Observable.just(1, 2, 3),
                new BiPredicate<Integer, Integer>() {
                    @Override
                    public boolean test(Integer integer, Integer integer2) throws Exception {
                        return integer.equals(integer2);
                    }
                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                Log.i(TAG, "---sequenceEqual---aBoolean: " + aBoolean);
            }
        });

        Observable.intervalRange(1, 9, 0, 1, TimeUnit.MICROSECONDS).skipUntil(Observable.timer(4,
                TimeUnit.MICROSECONDS)).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.i(TAG, "---skipUntil---aLong: " + aLong);
            }
        });

        Observable.just(1, 2, 3, 4).skipWhile(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer <= 2;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "---skipWhile---integer: " + integer);
            }
        });

        Observable.just(1, 2, 3, 4, 5, 6).takeUntil(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer >= 5;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "---takeUntil---integer: " + integer);
            }
        });

        Observable.just(1, 2, 3, 4, 5).takeWhile(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer < 3;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "---takeWhile---integer: " + integer);
            }
        });
    }

    private void debounceAction() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                if (emitter.isDisposed()) {
                    return;
                }
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
                            Log.i(TAG,
                                    stringIntegerGroupedObservable.getKey() + " member: " + integer);
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
                Log.i(TAG,
                        "---map1 opration---current thread: " + Thread.currentThread().getName());
                return s + " RxJava";
            }
        }).subscribeOn(Schedulers.single()).map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                Log.i(TAG,
                        "---map2 opration---current thread: " + Thread.currentThread().getName());
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
