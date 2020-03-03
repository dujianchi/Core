package cn.dujc.coreapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.dujc.core.gson.GodTypeAdapterFactory;
import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.util.GsonUtil;
import cn.dujc.coreapp.R;
import cn.dujc.coreapp.entity.Bean;
import cn.dujc.coreapp.entity.Bean1;

public class GsonActivity extends BaseActivity {

    private Gson mGson;
    private EditText mEtJson;
    private TextView mTvJson;

    @Override
    public int getViewId() {
        return R.layout.activity_gson;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        mGson = GodTypeAdapterFactory.createBuilder().create();
        mEtJson = findViewById(R.id.et_json);
        mTvJson = findViewById(R.id.tv_json);
    }

    public void generate(View v) {
        Bean<Bean1> bean = new Bean<>();
        bean.setCode("123");
        bean.setMsg("msg");
        List<Bean1> data = new ArrayList<>();
        bean.setData(data);
        Bean1 bean1 = new Bean1();
        bean1.setApply_read_status("Apply_read_status");
        bean1.setBaonum("50");
        bean1.setBarcode("123456456");
        bean1.setComplete_time("2012-12-12");
        bean1.setId("0");
        bean1.setIs_print("true");
        bean1.setIs_read("0");
        bean1.setItem_result_en(1);
        bean1.setItem_result_hint("setItem_result_hint");
        bean1.setOrders_code("setOrders_code");
        bean1.setOrders_status("false");
        bean1.setOtype("setOtype");
        bean1.setPdf(Arrays.asList("asdf", "123", "!#%&"));
        bean1.setPnames_str("setPnames_str");
        bean1.setRead_status("no");
        bean1.setReport_url("http://host/path");
        bean1.setStatus("1");
        bean1.setTest_name("setTest_name");
        bean1.setTest_phone("18059045652");
        bean1.setTest_sex("female");
        bean1.setCode(bean.getCode());
        bean1.setMsg(bean.getMsg());
        bean1.setData(Collections.<String>emptyList());
        data.add(bean1);
        mEtJson.setText(GsonUtil.toJsonString(bean));
    }

    public void regenerate(View v) {
        String json = mEtJson.getText().toString();
        Bean<Bean1> from = mGson.fromJson(json, new TypeToken<Bean<Bean1>>() {
        }.getType());
        mTvJson.setText(GsonUtil.toJsonString(from));
    }
}
