package cn.dujc.core.initializer.content;

import android.view.View;

/**
 * Activity、fragment、dialog等控件的rootView设置
 */
public interface IRootViewSetup {

    public void setup(Object target, View rootView);
}
