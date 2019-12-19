package cn.dujc.widget.resizeable;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

public class ResizeableButton extends AppCompatButton implements ResizeableText<AppCompatButton> {

    private final ResizeableText<AppCompatButton> mResizeableText;

    public ResizeableButton(Context context) {
        this(context, null, 0);
    }

    public ResizeableButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ResizeableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mResizeableText = new ResizeableTextImpl<AppCompatButton>(this);
        updateScaleFromGlobal(context);
    }

    @Override
    public AppCompatButton getTextView() {
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
