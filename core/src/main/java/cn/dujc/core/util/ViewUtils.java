package cn.dujc.core.util;

import android.view.MotionEvent;
import android.view.View;

/**
 * 控件方法封装
 */
public class ViewUtils {

    /**
     * 判断触摸事件是否在一个控件的位置内
     */
    public static boolean isViewUnderEvent(View view, MotionEvent event) {
        int[] xy = new int[2];
        view.getLocationOnScreen(xy);
        int width = view.getWidth();
        int height = view.getHeight();
        float x = event.getRawX();
        float y = event.getRawY();
        if (x < xy[0] || y < xy[1] || x > xy[0] + width || y > xy[1] + height) {
            return false;
        }
        return true;
    }

}
