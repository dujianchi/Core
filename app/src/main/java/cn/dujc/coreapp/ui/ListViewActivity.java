package cn.dujc.coreapp.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import cn.dujc.core.adapter.BaseAdapterL;
import cn.dujc.core.ui.BaseActivity;
import cn.dujc.coreapp.R;

public class ListViewActivity extends BaseActivity {

    private ListView mListView;

    @Override
    public int getViewId() {
        return R.layout.activity_list_view;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        mListView = findViewById(R.id.lv_list);
        List<String> list = Arrays.asList("aaa", "bbb", "ccc", "ddd");
        MyAdapter adapter = new MyAdapter(list);
        mListView.setAdapter(adapter);
    }

    private static class MyAdapter extends BaseAdapterL<String> {
        public MyAdapter(List<? extends String> mList) {
            super(mList);
        }

        @Override
        public View createView(ViewGroup parent) {
            return new TextView(parent.getContext());
        }

        @Override
        public void convert(String item, int position, ViewHolder holder) {
            ((TextView) holder.getConvertView()).setText(item);
        }
    }
}
