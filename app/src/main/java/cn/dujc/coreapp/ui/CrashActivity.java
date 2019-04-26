package cn.dujc.coreapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import cn.dujc.core.ui.BaseActivity;

public class CrashActivity extends BaseActivity implements View.OnClickListener {

    @Override
    public int getViewId() {
        return 0;
    }

    @Nullable
    @Override
    public View getViewV() {
        TextView textView = new TextView(mActivity);
        textView.setText("click to crash");
        textView.setGravity(Gravity.CENTER);
        textView.setOnClickListener(this);
        return textView;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View v) {
        System.out.println(((String) null).equals(null));
    }
}
