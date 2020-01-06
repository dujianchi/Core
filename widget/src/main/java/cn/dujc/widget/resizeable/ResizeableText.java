package cn.dujc.widget.resizeable;

import android.content.Context;
import android.widget.TextView;

/**
 * 可以重置大小的TextView相关实现方案
 */
public interface ResizeableText<T extends TextView> {
    public T getTextView();

    public void updateScale(float scale);

    public void updateScaleFromGlobal(Context context);
}
