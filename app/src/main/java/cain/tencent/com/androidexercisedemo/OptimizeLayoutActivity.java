package cain.tencent.com.androidexercisedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;

public class OptimizeLayoutActivity extends AppCompatActivity implements View.OnClickListener {
    Button moreBtn;

    private EditText editExtra1;
    private EditText editExtra2;
    private EditText editExtra3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optimize_layout);
        moreBtn = findViewById(R.id.more);
        moreBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more:
                onMoreClick();
                break;
        }
    }

    private void onMoreClick() {
        ViewStub viewStub = findViewById(R.id.view_stub);
        if(viewStub != null){
            View inflate = viewStub.inflate();
            editExtra1 = inflate.findViewById(R.id.edit_extra1);
            editExtra2 = inflate.findViewById(R.id.edit_extra2);
            editExtra3 = inflate.findViewById(R.id.edit_extra3);
        }
    }
}
