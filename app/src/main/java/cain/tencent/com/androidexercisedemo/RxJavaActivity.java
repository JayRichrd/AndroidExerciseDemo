package cain.tencent.com.androidexercisedemo;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cain.tencent.com.androidexercisedemo.databinding.ActivityRxJavaBinding;
import cain.tencent.com.androidexercisedemo.databinding.ActivityTipsBinding;
import cain.tencent.com.androidexercisedemo.domain.Address;
import cain.tencent.com.androidexercisedemo.domain.User;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;

public class RxJavaActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityRxJavaBinding databing;
    public static final String TAG = "RxJavaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databing = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_rx_java, null, false);
        setContentView(databing.getRoot());
        databing.btnRxjava.setOnClickListener(this);
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
        switchOpration();
    }

    private void switchOpration() {
        Address address1 = new Address("ren ming road", "ShenZhen");
        Address address2 = new Address("bai yun road", "GuangZhou");
        List<Address> addressList = new ArrayList<>();
        addressList.add(address1);
        addressList.add(address2);
        User user = new User("tony", addressList);

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
