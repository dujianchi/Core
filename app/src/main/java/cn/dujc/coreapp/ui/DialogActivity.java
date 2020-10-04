package cn.dujc.coreapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.ui.BaseDialog;
import cn.dujc.coreapp.R;

public class DialogActivity extends BaseActivity {

    private MyDialog mDialog;

    @Override
    public int getViewId() {
        return R.layout.activity_dialog;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {

    }

    public void showDialog(View v) {
        if (mDialog == null) mDialog = new MyDialog(mActivity);
        if (!mDialog.isShowing()) mDialog.show();
    }

    private static class MyDialog extends BaseDialog {

        public MyDialog(Context context) {
            super(context);
        }

        @Override
        public int getViewId() {
            return R.layout.dialog_my;
        }

        @Override
        public void initBasic(Bundle savedInstanceState) {

        }
    }
}
