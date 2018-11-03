package cain.tencent.com.androidexercisedemo;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import cain.tencent.com.androidexercisedemo.databinding.ActivityTipsBinding;

public class TipsActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityTipsBinding databing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databing = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_tips,
                null, false);
        setContentView(databing.getRoot());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dialog:
                break;
            case R.id.btn_tips:
                break;
            case R.id.btn_snackbar:
                break;
        }
    }
}
