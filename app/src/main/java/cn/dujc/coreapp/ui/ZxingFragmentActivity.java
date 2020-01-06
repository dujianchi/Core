package cn.dujc.coreapp.ui;

import android.os.Bundle;

import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.util.ToastUtil;
import cn.dujc.coreapp.R;
import cn.dujc.zxing.impl.ZxingFragment;
import cn.dujc.zxing.open.ICaptureResult;

public class ZxingFragmentActivity extends BaseActivity {

    @Override
    public int getViewId() {
        return R.layout.activity_zxing_fragment;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        final ZxingFragment fragment = new ZxingFragment();
        fragment.setCaptureResult(new ICaptureResult() {
            @Override
            public boolean handleDecode(String result) {
                ToastUtil.showToast(mActivity, result);
                fragment.resetScanMode();
                return false;
            }
        });
        showFragment(R.id.fl_zxing_container, fragment);
    }
}
