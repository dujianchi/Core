package cn.dujc.coreapp.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Iterator;

import cn.dujc.core.bridge.ActivityStackUtil;
import cn.dujc.core.bridge.IEvent;
import cn.dujc.core.ui.BaseFragment;
import cn.dujc.core.util.LogUtil;
import cn.dujc.core.util.ToastUtil;
import cn.dujc.coreapp.R;

public class FragmentChild extends BaseFragment implements IEvent {

    public static FragmentChild newInstance(int index) {
        Bundle args = new Bundle();
        args.putInt("abc", index);
        FragmentChild fragment = new FragmentChild();
        fragment.setArguments(args);
        return fragment;
    }

    private Fragment[] mFragments = new Fragment[]{FragmentGrandChild.newInstance(0), FragmentGrandChild.newInstance(1)};
    private TextView mTvText;
    private int mInt = 0;

    @Override
    public int getViewId() {
        return R.layout.fragment_child;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        mTvText = findViewById(R.id.tv_text);
        findViewById(R.id.ll_layout).setBackgroundColor(extras().get("abc", 0) % 2 == 0 ? Color.RED : Color.BLUE);
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(R.id.fl_container_child, mFragments[mInt++ % 2]);
            }
        });
    }

    @Override
    public void onMyEvent(int flag, Object value) {
        Iterator<Activity> iterator = ActivityStackUtil.getInstance().getActivityIterator();
        LogUtil.d("----------- " + iterator);
        StringBuilder stacks = new StringBuilder();
        while (iterator.hasNext()) {
            stacks.append(iterator.next().getClass().getSimpleName()).append("\n");
        }
        LogUtil.d("----------- " + stacks + "flag = " + flag);
        mTvText.setText(stacks);
        if (flag == 12) {
            ToastUtil.showToast(mActivity, "fragment: " + value);
        } else if (flag == 0) {
            starter()
                    .go(MainActivity.class);
        }
    }
}
