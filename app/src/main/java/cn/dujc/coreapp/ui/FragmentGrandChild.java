package cn.dujc.coreapp.ui;

import android.graphics.Color;
import android.os.Bundle;

import cn.dujc.core.ui.BaseFragment;
import cn.dujc.coreapp.R;

public class FragmentGrandChild extends BaseFragment {

    public static FragmentGrandChild newInstance(int index) {
        Bundle args = new Bundle();
        args.putInt("abc", index);
        FragmentGrandChild fragment = new FragmentGrandChild();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getViewId() {
        return R.layout.fragment_child;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        findViewById(R.id.ll_layout).setBackgroundColor(extras().get("abc", 0) % 2 == 0 ? Color.RED : Color.BLUE);
    }
}
