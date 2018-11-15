package cain.tencent.com.androidexercisedemo;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import cain.tencent.com.androidexercisedemo.databinding.ActivityTipsBinding;

public class TipsActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityTipsBinding databing;
    public static final String TAG = "TipsActivity";
    public static int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databing = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_tips, null, false);
        setContentView(databing.getRoot());
        databing.btnDialog.setOnClickListener(this);
        databing.btnSnackbar.setOnClickListener(this);
        databing.btnToast.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dialog:
                showDialog();
                break;
            case R.id.btn_toast:
                showToast(count++);
                break;
            case R.id.btn_snackbar:
                showSnackbar();
                break;
        }
    }

    private void showSnackbar() {
        Snackbar.make(databing.btnSnackbar, "snackBar", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Press snackBar.");
            }
        }).show();
    }

    private void showToast(int count) {
        CommonUtils.showLongToast(this, count + "");
    }

    private void showDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Title").setMessage("Dialog content").setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Log.d(TAG, "Press ok option.");
//            }
//        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Log.d(TAG, "Press cancel option");
//            }
//        });
//        builder.show();

        FirstRechargeDialog firstRechargeDialog = new FirstRechargeDialog(this,1,0,"");
        firstRechargeDialog.show();
        WindowManager windowManager = getWindowManager();
        Display defaultDisplay = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams attributes = firstRechargeDialog.getWindow().getAttributes();
        attributes.width = defaultDisplay.getWidth();
        firstRechargeDialog.getWindow().setAttributes(attributes);

    }


}
