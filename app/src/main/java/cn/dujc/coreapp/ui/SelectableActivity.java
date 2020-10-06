package cn.dujc.coreapp.ui;

import android.os.Bundle;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.util.ToastUtil;
import cn.dujc.coreapp.R;
import cn.dujc.widget.selectable.OnSelectChangedListener;
import cn.dujc.widget.selectable.SelectableLayout;

public class SelectableActivity extends BaseActivity {

    @Override
    public int getViewId() {
        return R.layout.activity_selectable;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        SelectableLayout selectableLayout = findViewById(R.id.sl_bottom);
        selectableLayout.setOnSelectChangedListener(new OnSelectChangedListener() {
            @Override
            public void onSelected(@NotNull List<? extends View> selectedViews, @NotNull Integer[] selectedIndexes, int lastIndex) {
                ToastUtil.showToast(mActivity, Arrays.toString(selectedIndexes));
            }
        });
    }
}
