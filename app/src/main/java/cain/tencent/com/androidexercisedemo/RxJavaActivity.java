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

import cain.tencent.com.androidexercisedemo.databinding.ActivityRxJavaBinding;
import cain.tencent.com.androidexercisedemo.domain.Address;
import cain.tencent.com.androidexercisedemo.domain.User;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
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
        groupAction();
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
//                Log.i(TAG, "group name: " + stringIntegerGroupedObservable.getKey());
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
