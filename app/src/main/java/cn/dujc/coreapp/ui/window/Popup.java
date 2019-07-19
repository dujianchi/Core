package cn.dujc.coreapp.ui.window;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import cn.dujc.core.ui.BasePopupWindow;
import cn.dujc.coreapp.R;

public class Popup extends BasePopupWindow {

    public Popup(Context context) {
        super(context);
    }

    @Override
    public int getViewId() {
        return 0;
    }

    @Override
    public View getViewV() {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(R.mipmap.ic_launcher);
        return imageView;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        ((ImageView)mRootView).setImageResource(R.mipmap.ic_star_red);
    }
}
