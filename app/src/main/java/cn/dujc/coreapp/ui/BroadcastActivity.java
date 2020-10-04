package cn.dujc.coreapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.util.ToastUtil;
import cn.dujc.coreapp.R;

public class BroadcastActivity extends BaseActivity {

    private Button mBtnSend;
    private TextView mTvText;
    private MyBroadcastReceiver mReceiver;

    @Override
    public int getViewId() {
        return R.layout.activity_broadcast;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mTvText = (TextView) findViewById(R.id.tv_text);

        final String action = "aaaaa";

        registerReceiver(mReceiver = new MyBroadcastReceiver(), new IntentFilter(action));

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(mActivity, "wait 2 seconds");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        sendBroadcast(new Intent(action));
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    public static class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ((BroadcastActivity) context).mTvText.setText("afadsfsdfsdf");
        }
    }
}
