package cn.dujc.coreapp.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import cn.dujc.core.ui.BaseFragment;
import cn.dujc.coreapp.R;

public class FragmentChild extends BaseFragment {

    public static FragmentChild newInstance(int index) {
        Bundle args = new Bundle();
        args.putInt("abc", index);
        FragmentChild fragment = new FragmentChild();
        fragment.setArguments(args);
        return fragment;
    }

    private Fragment[] mFragments = new Fragment[]{FragmentGrandChild.newInstance(0), FragmentGrandChild.newInstance(1)};
    private int mInt = 0;

    @Override
    public int getViewId() {
        return R.layout.fragment_child;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        findViewById(R.id.ll_layout).setBackgroundColor(extras().get("abc", 0) % 2 == 0 ? Color.RED : Color.BLUE);
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(R.id.fl_container_child, mFragments[mInt++ % 2]);
            }
        });
    }
}
