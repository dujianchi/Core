package cn.dujc.widget.resizeable;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class ResizeableTextView extends AppCompatTextView implements ResizeableText<AppCompatTextView> {

    private final ResizeableText<AppCompatTextView> mResizeableText;

    public ResizeableTextView(Context context) {
        this(context, null, 0);
    }

    public ResizeableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ResizeableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mResizeableText = new ResizeableTextImpl<AppCompatTextView>(this);
        updateScaleFromGlobal(context);
    }

    @Override
    public AppCompatTextView getTextView() {
        return this;
    }

    public void updateScale(float scale) {
        mResizeableText.updateScale(scale);
    }

    @Override
    public void updateScaleFromGlobal(Context context) {
        mResizeableText.updateScaleFromGlobal(context);
    }

}
