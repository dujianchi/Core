package cn.dujc.coreapp.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import cn.dujc.core.ui.TitleCompat;
import cn.dujc.core.ui.base.SingleTaskActivity;
import cn.dujc.coreapp.R;

public class SplashActivity extends SingleTaskActivity {

    @Nullable
    @Override
    public View initToolbar(ViewGroup parent) {
        return null;
    }

    @Override
    protected boolean fullScreen() {
        return true;
    }

    @Nullable
    @Override
    public TitleCompat initTransStatusBar() {
        return null;
    }

    @Override
    public void onTaskExecute() {
        starter().go(MainActivity.class, true);
    }

    @Override
    public int getViewId() {
        return 0;
    }

    @Nullable
    @Override
    public View getViewV() {
        ImageView imageView = new ImageView(mActivity);
        imageView.setImageResource(R.mipmap.splash);
        return imageView;
    }
}
