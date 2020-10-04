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
import cn.dujc.coreapp.entity.Bean2;

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
        bean.setCode(123);
        bean.setMsg("msg");
        List<Bean1> data = new ArrayList<>();
        bean.setData(data);
        Bean1 bean1 = new Bean1();
        bean1.setBooleanArrays(new boolean[]{true, false, true});
        bean1.setFloatList(Arrays.asList(1F, 2F, 3F));
        bean1.setIntArrays(new int[]{3, 4, 5});
        bean1.setStringArrays(new String[]{"123", "456"});
        bean1.setStringList(Arrays.asList("abc", "def"));
        bean1.setStringStringMap(Collections.singletonMap("fuck", "you"));
        data.add(bean1);
        mEtJson.setText(GsonUtil.toJsonString(bean));
    }

    public void regenerate(View v) {
        String json = mEtJson.getText().toString();
        Bean<Bean2> from = mGson.fromJson(json, new TypeToken<Bean<Bean2>>() {
        }.getType());
        mTvJson.setText(GsonUtil.toJsonString(from));
        System.out.println(from.getCode());
        System.out.println(from.getMsg());
        System.out.println(from.getData());
        System.out.println(from.getData().get(0).isBooleanArrays() + "");
        System.out.println(from.getData().get(0).getFloatList() + "");
        System.out.println(Arrays.toString(from.getData().get(0).getIntArrays()) + "");
        System.out.println(from.getData().get(0).getStringArrays() + "");
        System.out.println(from.getData().get(0).getStringList() + "");
        System.out.println(from.getData().get(0).getStringStringMap());
    }
}
