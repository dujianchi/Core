package cn.dujc.coreapp.ui;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import cn.dujc.core.ui.BaseActivity;
import cn.dujc.coreapp.R;

public class FragmentsActivity extends BaseActivity {

    private Fragment[] mFragments = new Fragment[]{FragmentChild.newInstance(0), FragmentChild.newInstance(1)};
    private int mInt = 0;

    @Override
    public int getViewId() {
        return R.layout.activity_fragment;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(R.id.fl_container, mFragments[mInt++ % 2], "tag" + (mInt % 2));
            }
        });
    }

}
