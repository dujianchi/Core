package cn.dujc.coreapp.ui;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.util.StringUtil;
import cn.dujc.coreapp.R;
import cn.dujc.widget.wheelpicker.IWheelPicker;
import cn.dujc.widget.wheelpicker.WheelPicker;

public class WheelPickerActivity extends BaseActivity {

    private WheelPicker mWpPop0;
    private WheelPicker mWpPop1;

    @Override
    public int getViewId() {
        return R.layout.activity_wheel_picker;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        mWpPop0 = findViewById(R.id.wp_pop_0);
        mWpPop1 = findViewById(R.id.wp_pop_1);

        mWpPop0.setData(generateData(11));
        mWpPop1.setData(generateData(1));

        mWpPop0.setOnItemSelectedListener(new IWheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                mWpPop1.setData(generateData(1));
            }
        });
    }

    private List<String> generateData(int padding) {
        int size = new Random().nextInt(10) + padding;
        List<String> result = new ArrayList<>(size);
        for (int index = 0; index < size; index++) {
            result.add(StringUtil.random(10).toString());
        }
        return result;
    }
}
