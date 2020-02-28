package cn.dujc.coreapp.ui;

import android.os.Bundle;
import android.view.View;

import cn.dujc.core.bridge.ActivityStackUtil;
import cn.dujc.core.bridge.IEvent;
import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.util.ToastUtil;
import cn.dujc.coreapp.R;

public class EventActivity1 extends BaseActivity implements IEvent {

    private int mFlat = 12;

    @Override
    public int getViewId() {
        return R.layout.activity_event1;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        setTitle("send event");
        showFragment(R.id.fl_container, new FragmentChild());
    }

    public void send(View v) {
        ActivityStackUtil.getInstance().sendEvent(mFlat, "finish" + (mFlat--), ActivityStackUtil.ACTIVITY);
    }

    @Override
    public void onMyEvent(int flag, Object value) {
        if (flag == -1) ToastUtil.showToast(mActivity, "value");
    }
}
