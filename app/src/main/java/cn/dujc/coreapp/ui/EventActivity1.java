package cn.dujc.coreapp.ui;

import android.os.Bundle;
import android.view.View;

import cn.dujc.core.bridge.ActivityStackUtil;
import cn.dujc.core.bridge.IEvent;
import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.util.ToastUtil;
import cn.dujc.coreapp.R;

public class EventActivity1 extends BaseActivity implements IEvent {

    @Override
    public int getViewId() {
        return R.layout.activity_event1;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        setTitle("send event");
        showFragment(R.id.fl_container, new FragmentChild());
    }

    public void send12(View v) {
        ActivityStackUtil.getInstance().sendEvent(12, "flag:12", ActivityStackUtil.ACTIVITY);
    }

    public void send11(View v) {
        ActivityStackUtil.getInstance().sendEvent(11, "flag:11", ActivityStackUtil.ALL);
    }

    public void send10(View v) {
        ActivityStackUtil.getInstance().sendEvent(10, "flag:10", ActivityStackUtil.ALL);
    }

    public void send9(View v) {
        ActivityStackUtil.getInstance().sendEvent(9, "flag:9", ActivityStackUtil.ALL);
    }

    public void send8(View v) {
        ActivityStackUtil.getInstance().sendEvent(8, "flag:8", ActivityStackUtil.ALL);
    }

    public void send7(View v) {
        ActivityStackUtil.getInstance().sendEvent(7, "flag:7", ActivityStackUtil.ALL);
    }

    public void send6(View v) {
        ActivityStackUtil.getInstance().sendEvent(6, "flag:6", ActivityStackUtil.ALL);
    }

    public void send5(View v) {
        ActivityStackUtil.getInstance().sendEvent(5, "flag:5", ActivityStackUtil.ALL);
    }

    public void send4(View v) {
        ActivityStackUtil.getInstance().sendEvent(4, "flag:4", ActivityStackUtil.ALL);
    }

    public void send3(View v) {
        ActivityStackUtil.getInstance().sendEvent(3, "flag:3", ActivityStackUtil.ALL);
    }

    public void send2(View v) {
        ActivityStackUtil.getInstance().sendEvent(2, "flag:2", ActivityStackUtil.ALL);
    }

    public void send1(View v) {
        ActivityStackUtil.getInstance().sendEvent(1, "flag:1", ActivityStackUtil.ALL);
    }

    public void send0(View v) {
        ActivityStackUtil.getInstance().sendEvent(0, "flag:0", ActivityStackUtil.ALL);
    }

    public void send_1(View v) {
        ActivityStackUtil.getInstance().sendEvent(-1, "flag:-1", ActivityStackUtil.ALL);
    }

    @Override
    public void onMyEvent(int flag, Object value) {
        if (flag == -1) ToastUtil.showToast(mActivity, "value");
    }
}
