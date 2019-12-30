package cn.dujc.coreapp.ui;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.List;

import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.util.DeviceUtil;
import cn.dujc.core.util.LogUtil;
import cn.dujc.core.util.RichTextBuilder;
import cn.dujc.coreapp.R;

public class CrashActivity extends BaseActivity implements View.OnClickListener {

    @Override
    public int getViewId() {
        return 0;
    }

    @Nullable
    @Override
    public View getViewV() {
        TextView textView = new TextView(mActivity);
        textView.setText(new RichTextBuilder()
                .addTextWithDefault("12", "123")
                .addTextPart(mActivity, R.color.colorAccent, '2')
                .addTextPart("asdfasdf")
                .addTextPart('\n')
                .batch()
                .append(1).append("click").append(' ').append("to crash")
                .create(mActivity, R.color.colorPrimary)
                .addTextPart('\n')
                .addTextPartScale("2 scale", 2)
                .addTextPart('\n')
                .addPart("2 scale red ", new RelativeSizeSpan(2), new ForegroundColorSpan(Color.RED))
                .ifNotNone("aaa").append("aaa").create()
                .build());
        textView.setGravity(Gravity.CENTER);
        //textView.setOnClickListener(this);
        return textView;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        permissionKeeper().requestPermissionsNormal(123
                , Manifest.permission.BLUETOOTH
                , Manifest.permission.ACCESS_WIFI_STATE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_PHONE_STATE
        );
    }

    @Override
    public void onGranted(int requestCode, List<String> permissions) {
        String deviceId = DeviceUtil.getDeviceId(mActivity);
        LogUtil.d(deviceId);
    }

    @Override
    public void onClick(View v) {
        System.out.println(((String) null).equals(null));
    }
}
