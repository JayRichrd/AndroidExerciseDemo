package cain.tencent.com.androidexercisedemo;

import android.app.Dialog;
import android.content.Context;


/**
 * Created by luciolong on 2016/9/29.
 * 基本dialog类型，解决dismiss时crash问题
 */
public class BaseDemoDialog extends Dialog {
    public static final String TAG = "BaseDemoDialog";

    public BaseDemoDialog(Context context) {
        super(context);
    }

    public BaseDemoDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseDemoDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void show() {
        try {
            if (!this.isShowing()) {
                super.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void dismiss() {
        try {
            if (this.isShowing()) {
                super.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
