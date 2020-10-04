package cn.dujc.widget.resizeable;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.widget.TextView;

public class ResizeableTextImpl<T extends TextView> implements ResizeableText<T> {

    private static final String KEY_SCALE = "KEY_SCALE";
    private final T mTextView;
    private float mScale = 1.0F;

    public ResizeableTextImpl(T textView) {
        mTextView = textView;
    }

    public static void setGlobalScale(Context context, float scale) {
        if (context != null && scale > 0) {
            getPreferences(context).edit()
                    .putFloat(KEY_SCALE, scale)
                    .apply();
        }
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(ResizeableTextImpl.class.getName(), Context.MODE_PRIVATE);
    }

    @Override
    public T getTextView() {
        return mTextView;
    }

    @Override
    public void updateScale(float scale) {
        getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextView().getTextSize() / mScale * scale);
        mScale = scale;
    }

    @Override
    public void updateScaleFromGlobal(Context context) {
        float scale = getPreferences(context).getFloat(KEY_SCALE, 1.0F);
        updateScale(scale);
    }
}
