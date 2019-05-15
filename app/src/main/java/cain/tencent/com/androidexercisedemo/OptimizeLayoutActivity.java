package cain.tencent.com.androidexercisedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;

public class OptimizeLayoutActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "OptimizeLayoutActivity";
    Button moreBtn;

    private EditText editExtra1;
    private EditText editExtra2;
    private EditText editExtra3;
    int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optimize_layout);
        moreBtn = findViewById(R.id.more);
        moreBtn.setOnClickListener(this);
        value = 1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more:
                onMoreClick();
                conditionDebug();
                break;
        }
    }

    private void onMoreClick() {
        ViewStub viewStub = findViewById(R.id.view_stub);
        if (viewStub != null) {
            View inflate = viewStub.inflate();
            editExtra1 = inflate.findViewById(R.id.edit_extra1);
            editExtra2 = inflate.findViewById(R.id.edit_extra2);
            editExtra3 = inflate.findViewById(R.id.edit_extra3);
        }
    }

    private void conditionDebug() {
        for (int i = 1; i < 10; i++) {
            Log.d(TAG, "i = " + i);
        }
        value =2;
    }
}
