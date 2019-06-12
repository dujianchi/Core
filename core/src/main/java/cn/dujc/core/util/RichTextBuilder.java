package cn.dujc.core.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.view.View;

/**
 * @author du
 * date 2018/7/27 下午1:42
 */
public class RichTextBuilder {

    public interface OnClickListener {
        void onClick(View widget, CharSequence clickedText);
    }

    /**
     * 点击事件，需要与{@link android.text.method.LinkMovementMethod}
     * 或{@link cn.dujc.core.impls.LinkMovementMethodReplacement}
     * 配合使用
     */
    public static class TextClickableSpan extends ClickableSpan {

        private final CharSequence mText;
        private final int mColor;
        private final OnClickListener mOnClickListener;

        public TextClickableSpan(CharSequence text, int color, OnClickListener onClickListener) {
            mText = text;
            mColor = color;
            mOnClickListener = onClickListener;
        }

        @Override
        public void onClick(View widget) {
            if (mOnClickListener != null) mOnClickListener.onClick(widget, mText);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            //super.updateDrawState(ds);
            if (mColor != 0) ds.setColor(mColor);
        }
    }

    /**
     * 批量添加
     */
    public static class Texts {
        private final RichTextBuilder mBuilder;
        private final StringBuilder mText;

        private Texts(RichTextBuilder builder) {
            mText = new StringBuilder();
            mBuilder = builder;
        }

        public Texts append(boolean b) {
            mText.append(b);
            return this;
        }

        public Texts append(char c) {
            mText.append(c);
            return this;
        }

        public Texts append(int i) {
            mText.append(i);
            return this;
        }

        public Texts append(long l) {
            mText.append(l);
            return this;
        }

        public Texts append(float f) {
            mText.append(f);
            return this;
        }

        public Texts append(double d) {
            mText.append(d);
            return this;
        }

        public Texts append(Object o) {
            mText.append(o);
            return this;
        }

        public Texts append(String str) {
            mText.append(str);
            return this;
        }

        public Texts append(StringBuffer sb) {
            mText.append(sb);
            return this;
        }

        public Texts append(char[] chars) {
            mText.append(chars);
            return this;
        }

        public Texts append(CharSequence csq) {
            mText.append(csq);
            return this;
        }

        public RichTextBuilder create() {
            return mBuilder;
        }

        public RichTextBuilder create(Context context, int colorId) {
            return mBuilder.addTextPart(context, colorId, mText);
        }

        public RichTextBuilder create(int color) {
            return mBuilder.addTextPart(color, mText);
        }

        /**
         * 添加一个带文本样式的文字，比如删除线{@link android.text.style.StrikethroughSpan}
         * 、下划线{@link android.text.style.UnderlineSpan}
         * 、下标{@link android.text.style.SubscriptSpan}
         * 、上标{@link android.text.style.SuperscriptSpan}
         */
        public RichTextBuilder create(CharacterStyle characterStyle) {
            return mBuilder.addTextPart(mText, characterStyle);
        }

        public RichTextBuilder create(Context context, int colorId, OnClickListener listener) {
            return mBuilder.addTextPart(mText, context, colorId, listener);
        }

        public RichTextBuilder create(int color, OnClickListener listener) {
            return mBuilder.addTextPart(mText, color, listener);
        }

        /**
         * 添加一个指定多少px大小的文字
         */
        public RichTextBuilder createPx(int sizeInPx) {
            return mBuilder.addTextPartPx(mText, sizeInPx);
        }

        /**
         * 添加一个指定多少dp大小的文字
         */
        public RichTextBuilder createDp(int sizeInDp) {
            return mBuilder.addTextPartPx(mText, sizeInDp);
        }

        /**
         * 添加一个指定倍数的文字，比如0.5即为设置的字体的一半大
         */
        public RichTextBuilder createScale(float scale) {
            return mBuilder.addTextPartScale(mText, scale);
        }

        /**
         * 添加一个指定宽度倍数的文字，比如2。0即为设置的字体的2倍宽
         */
        public RichTextBuilder createScaleX(float scaleX) {
            return mBuilder.addTextPartScaleX(mText, scaleX);
        }

    }

    private final SpannableStringBuilder mStringBuilder = new SpannableStringBuilder();

    /**
     * 返回spannable
     */
    public Spannable build() {
        return mStringBuilder;
    }

    /**
     * 添加一个char字符
     */
    public RichTextBuilder addTextPart(char text) {
        mStringBuilder.append(text);
        return this;
    }

    /**
     * 添加多个char字符
     */
    public RichTextBuilder addTexts(char... texts) {
        if (texts != null) {
            for (final char text : texts) {
                addTextPart(text);
            }
        }
        return this;
    }

    /**
     * 添加一个纯文本
     */
    public RichTextBuilder addTextPart(CharSequence text) {
        if (!TextUtils.isEmpty(text)) mStringBuilder.append(text);
        return this;
    }

    /**
     * 添加多个char字符
     */
    public RichTextBuilder addTexts(CharSequence... texts) {
        if (texts != null) {
            for (final CharSequence text : texts) {
                addTextPart(text);
            }
        }
        return this;
    }

    /**
     * 添加一个带颜色的char字符
     */
    public RichTextBuilder addTextPart(Context context, int colorId, char text) {
        return addTextPart(context, colorId, String.valueOf(text));
    }

    /**
     * 添加一个带颜色的char字符
     */
    public RichTextBuilder addTextPart(int color, char text) {
        return addTextPart(color, String.valueOf(text));
    }

    /**
     * 添加一个带颜色的文字
     */
    public RichTextBuilder addTextPart(Context context, int colorId, CharSequence text) {
        if (context == null || colorId == 0) return addTextPart(text);
        return addPart(text, new ForegroundColorSpan(ContextCompat.getColor(context, colorId)));
    }

    /**
     * 添加一个带颜色的文字
     */
    public RichTextBuilder addTextPart(int color, CharSequence text) {
        return addPart(text, new ForegroundColorSpan(color));
    }

    /**
     * 添加一个带文本样式的文字，比如删除线{@link android.text.style.StrikethroughSpan}
     * 、下划线{@link android.text.style.UnderlineSpan}
     * 、下标{@link android.text.style.SubscriptSpan}
     * 、上标{@link android.text.style.SuperscriptSpan}
     */
    public RichTextBuilder addTextPart(CharSequence text, CharacterStyle characterStyle) {
        return addPart(text, characterStyle);
    }

    /**
     * 添加一个带颜色和点击事件的文字
     */
    public RichTextBuilder addTextPart(CharSequence text, Context context, int colorId, OnClickListener listener) {
        return addPart(text, new TextClickableSpan(text, ContextCompat.getColor(context, colorId), listener));
    }

    /**
     * 添加一个带颜色和点击事件的文字
     */
    public RichTextBuilder addTextPart(CharSequence text, int color, OnClickListener listener) {
        return addPart(text, new TextClickableSpan(text, color, listener));
    }

    /**
     * 添加一个指定多少px大小的文字
     */
    public RichTextBuilder addTextPartPx(CharSequence text, int sizeInPx) {
        return addPart(text, new AbsoluteSizeSpan(sizeInPx));
    }

    /**
     * 添加一个指定多少dp大小的文字
     */
    public RichTextBuilder addTextPartDp(CharSequence text, int sizeInDp) {
        return addPart(text, new AbsoluteSizeSpan(sizeInDp, true));
    }

    /**
     * 添加一个指定倍数的文字，比如0.5即为设置的字体的一半大
     */
    public RichTextBuilder addTextPartScale(CharSequence text, float scale) {
        return addPart(text, new RelativeSizeSpan(scale));
    }

    /**
     * 添加一个指定宽度倍数的文字，比如2。0即为设置的字体的2倍宽
     */
    public RichTextBuilder addTextPartScaleX(CharSequence text, float scaleX) {
        return addPart(text, new ScaleXSpan(scaleX));
    }

    /**
     * 添加一个图片
     */
    public RichTextBuilder addImage(Context context, int drawableId) {
        if (drawableId == 0) return this;
        return addImage(ContextCompat.getDrawable(context, drawableId));
    }

    /**
     * 添加一个图片
     */
    public RichTextBuilder addImage(Drawable drawable) {
        if (drawable == null) return this;
        return addImage(drawable, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), DynamicDrawableSpan.ALIGN_BASELINE);
    }

    /**
     * 添加一个图片
     *
     * @param verticalAlignment 选择{@link android.text.style.DynamicDrawableSpan#ALIGN_BASELINE}
     *                          或{@link android.text.style.DynamicDrawableSpan#ALIGN_BOTTOM}
     */
    public RichTextBuilder addImage(Drawable drawable, int verticalAlignment) {
        if (drawable == null) return this;
        return addImage(drawable, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), verticalAlignment);
    }

    /**
     * 添加一个图片
     *
     * @param verticalAlignment 选择{@link android.text.style.DynamicDrawableSpan#ALIGN_BASELINE}
     *                          或{@link android.text.style.DynamicDrawableSpan#ALIGN_BOTTOM}
     */
    public RichTextBuilder addImage(Drawable drawable, int width, int height, int verticalAlignment) {
        if (drawable == null) return this;
        drawable.setBounds(0, 0, width, height);
        return addPart(" ", new ImageSpan(drawable, verticalAlignment));
    }

    /**
     * 添加一个文字，并指定span
     */
    public RichTextBuilder addPart(CharSequence text, Object span) {
        if (!TextUtils.isEmpty(text) && span != null) {
            final int start = mStringBuilder.length();
            final int end = start + text.length();
            mStringBuilder.append(text);
            mStringBuilder.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return this;
    }

    /**
     * 对多个对象批量组合
     */
    public Texts batch() {
        return new Texts(this);
    }

}
