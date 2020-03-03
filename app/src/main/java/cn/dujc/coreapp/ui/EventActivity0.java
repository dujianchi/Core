package cn.dujc.coreapp.ui;

import android.os.Bundle;
import android.view.View;

import cn.dujc.core.bridge.ActivityStackUtil;
import cn.dujc.core.bridge.IEvent;
import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.util.StringUtil;
import cn.dujc.core.util.ToastUtil;
import cn.dujc.coreapp.R;

public class EventActivity0 extends BaseActivity implements IEvent {

    public static final String EXTRA_INDEX = "index";
    private int mIndex;

    @Override
    public int getViewId() {
        return R.layout.activity_event0;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        mIndex = extras().get(EXTRA_INDEX, 0);
        setTitle(StringUtil.concat("middle", mIndex));
    }

    public void next(View v) {
        if (mIndex < 5) {
            starter()
                    .with(EXTRA_INDEX, mIndex + 1)
                    .go(EventActivity0.class);
        } else {
            starter().go(EventActivity1.class);
        }
    }

    @Override
    public void onMyEvent(int flag, Object value) {
        //ToastUtil.showToast(mActivity, String.valueOf(value));
        if (flag == 1) {
            ActivityStackUtil.getInstance().finishUntil(MainActivity.class);
        } else if (flag == 2) {
            ActivityStackUtil.getInstance().keepOneSurvivalAndOther(this, MainActivity.class);
        } else if (flag == 3) {
            ActivityStackUtil.getInstance().closeAllActivity();
        } else if (flag == 4) {
            ActivityStackUtil.getInstance().closeAllExcept(MainActivity.class);
        } else if (flag == 5) {
            ActivityStackUtil.getInstance().finishActivity(EventActivity1.class, MainActivity.class);
        } else if (flag == 6 && mIndex == 4) {
            ActivityStackUtil.getInstance().finishSameButThis(this);
        } else if (flag == 7) {
            ActivityStackUtil.getInstance().sendEvent(2, "aaa", ActivityStackUtil.ACTIVITY);
        } else if (flag == 8) {
            ToastUtil.showToast(mActivity, ActivityStackUtil.getInstance().contains(ListActivity.class)
                    + "\n"
                    + ActivityStackUtil.getInstance().contains(MainActivity.class)
            );
        } else if (flag == 9 && mIndex == 4) {
            int i0 = ActivityStackUtil.getInstance().foregroundCount();
            ActivityStackUtil.getInstance().finishActivity(MainActivity.class);
            int i1 = ActivityStackUtil.getInstance().foregroundCount();
            ActivityStackUtil.getInstance().finishActivity(this);
            ToastUtil.showToast(ActivityStackUtil.getInstance().topActivity(), i0 + " -> " + i1 + " -> " + ActivityStackUtil.getInstance().foregroundCount());
        } else if (flag == 10) {
            ToastUtil.showToast(ActivityStackUtil.getInstance().topActivity()
                    , "main: " + ActivityStackUtil.getInstance().getActivity(MainActivity.class)
                            + "\nnull: " + ActivityStackUtil.getInstance().getActivity(ListActivity.class)
            );
        } else if (flag == 11) {
            ToastUtil.showToast(mActivity, "top:" + ActivityStackUtil.getInstance().topActivity());
        } else if (flag == 12) {
            ActivityStackUtil.getInstance().sendEvent(12, "fragment", ActivityStackUtil.FRAGMENT);
            //} else if (flag == 2) {
            //    ActivityStackUtil.getInstance();
        }
    }
}
