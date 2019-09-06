package cn.dujc.coreapp.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.util.RichTextBuilder;
import cn.dujc.coreapp.R;

public class CrashActivity extends BaseActivity implements View.OnClickListener {

    @Override
    public int getViewId() {
        return 0;
    }

    @Nullable
    @Override
    public View getViewV() {
        TextView textView = new TextView(mActivity);
        textView.setText(new RichTextBuilder()
                .addTextPart(mActivity, R.color.colorAccent, '2')
                .addTextPart("asdfasdf")
                .addTextPart('\n')
                .batch()
                .append(1).append("click").append(' ').append("to crash")
                .create(mActivity, R.color.colorPrimary)
                .addTextPart('\n')
                .addTextPartScale("2 scale", 2)
                .addTextPart('\n')
                .addPart("2 scale red ", new RelativeSizeSpan(2), new ForegroundColorSpan(Color.RED))
                .build());
        textView.setGravity(Gravity.CENTER);
        //textView.setOnClickListener(this);
        return textView;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View v) {
        System.out.println(((String) null).equals(null));
    }
}
