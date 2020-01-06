package cn.dujc.core.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
        private final Integer mColor;
        private final OnClickListener mOnClickListener;

        public TextClickableSpan(CharSequence text, Integer color, OnClickListener onClickListener) {
            mText = text;
            mColor = color;
            mOnClickListener = onClickListener;
        }

        @Override
        public void onClick(@NonNull View widget) {
            if (mOnClickListener != null) mOnClickListener.onClick(widget, mText);
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            if (mColor != null) ds.setColor(mColor);
            else super.updateDrawState(ds);
        }
    }

    /**
     * 批量添加
     */
    public static class Texts {
        final RichTextBuilder mBuilder;
        final StringBuilder mText;

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
            return mBuilder.addTextPart(mText);
        }

        public RichTextBuilder create(Context context, @ColorRes int colorId) {
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

        public RichTextBuilder create(Context context, @ColorRes int colorId, OnClickListener listener) {
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

    /**
     * 某x个变量不为空的时候的操作
     */
    public static class NotEmptyTexts extends Texts {

        private final CharSequence[] mTexts;

        private NotEmptyTexts(RichTextBuilder builder, CharSequence... texts) {
            super(builder);
            mTexts = texts;
        }

        private boolean isEmpty() {
            if (mTexts == null || mTexts.length == 0) return true;
            for (CharSequence text : mTexts) {
                if (TextUtils.isEmpty(text)) return true;
            }
            return false;
        }

        @Override
        public Texts append(boolean b) {
            if (isEmpty()) return this;
            return super.append(b);
        }

        @Override
        public Texts append(char c) {
            if (isEmpty()) return this;
            return super.append(c);
        }

        @Override
        public Texts append(int i) {
            if (isEmpty()) return this;
            return super.append(i);
        }

        @Override
        public Texts append(long l) {
            if (isEmpty()) return this;
            return super.append(l);
        }

        @Override
        public Texts append(float f) {
            if (isEmpty()) return this;
            return super.append(f);
        }

        @Override
        public Texts append(double d) {
            if (isEmpty()) return this;
            return super.append(d);
        }

        @Override
        public Texts append(Object o) {
            if (isEmpty()) return this;
            return super.append(o);
        }

        @Override
        public Texts append(String str) {
            if (isEmpty()) return this;
            return super.append(str);
        }

        @Override
        public Texts append(StringBuffer sb) {
            if (isEmpty()) return this;
            return super.append(sb);
        }

        @Override
        public Texts append(char[] chars) {
            if (isEmpty()) return this;
            return super.append(chars);
        }

        @Override
        public Texts append(CharSequence csq) {
            if (isEmpty()) return this;
            return super.append(csq);
        }
    }

    /**
     * 批量属性
     */
    public static class Styles {
        private final RichTextBuilder mBuilder;
        private final List<Object> mStyles;

        private Styles(RichTextBuilder builder) {
            mBuilder = builder;
            mStyles = new ArrayList<>();
        }

        /**
         * 添加颜色
         */
        public Styles addStyle(Context context, @ColorRes int colorId) {
            return addStyle(ContextCompat.getColor(context, colorId));
        }

        /**
         * 添加颜色
         */
        public Styles addStyle(int color) {
            mStyles.add(new ForegroundColorSpan(color));
            return this;
        }

        /**
         * 添加一个带文本样式的文字，比如删除线{@link android.text.style.StrikethroughSpan}
         * 、下划线{@link android.text.style.UnderlineSpan}
         * 、下标{@link android.text.style.SubscriptSpan}
         * 、上标{@link android.text.style.SuperscriptSpan}
         */
        public Styles addStyle(CharacterStyle characterStyle) {
            mStyles.add(characterStyle);
            return this;
        }

        /**
         * 添加字体大小
         */
        public Styles addStylePx(Context context, @DimenRes int sizeId) {
            if (context != null)
                mStyles.add(new AbsoluteSizeSpan(context.getResources().getDimensionPixelOffset(sizeId)));
            return this;
        }

        /**
         * 添加字体大小
         */
        public Styles addStylePx(int sizeInPx) {
            mStyles.add(new AbsoluteSizeSpan(sizeInPx));
            return this;
        }

        /**
         * 添加字体大小
         */
        public Styles addStyleDp(int sizeInDp) {
            mStyles.add(new AbsoluteSizeSpan(sizeInDp, true));
            return this;
        }

        /**
         * 添加字体大小比例
         */
        public Styles addStyleScale(float scale) {
            mStyles.add(new RelativeSizeSpan(scale));
            return this;
        }

        /**
         * 添加字体水平比例
         */
        public Styles addStyleScaleX(float scaleX) {
            mStyles.add(new ScaleXSpan(scaleX));
            return this;
        }

        /**
         * 添加内容
         */
        public RichTextBuilder fillContent(Object... texts) {
            StringBuilder text = new StringBuilder();
            if (texts != null && texts.length > 0) {
                for (Object obj : texts) {
                    if (obj != null) text.append(obj);
                }
                final int spanSize = mStyles.size();
                if (spanSize > 0 && text.length() > 0) {
                    Object[] spans = new Object[spanSize];
                    mStyles.toArray(spans);
                    mBuilder.addPart(text, spans);
                }
            }
            return mBuilder;
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
    public RichTextBuilder addTextPart(Context context, @ColorRes int colorId, char text) {
        return addTextPart(context, colorId, String.valueOf(text));
    }

    /**
     * 添加一个带颜色的char字符
     */
    public RichTextBuilder addTextPart(int color, char text) {
        return addTextPart(color, String.valueOf(text));
    }

    /**
     * 添加一个带默认值的变量，在值为null时使用默认值
     */
    public RichTextBuilder addTextWithDefault(Object text, Object _default) {
        return addTextPart(text == null ? _default == null ? "" : String.valueOf(_default) : String.valueOf(text));
    }

    /**
     * 添加一个带颜色的文字
     */
    public RichTextBuilder addTextPart(Context context, @ColorRes int colorId, CharSequence text) {
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
    public RichTextBuilder addTextPart(CharSequence text, Context context, @ColorRes int colorId, OnClickListener listener) {
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
    public RichTextBuilder addImage(Context context, @DrawableRes int drawableId) {
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
     *                          或{@link android.text.style.DynamicDrawableSpan#ALIGN_CENTER}(api > Q)
     */
    public RichTextBuilder addImage(Drawable drawable, int verticalAlignment) {
        if (drawable == null) return this;
        return addImage(drawable, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), verticalAlignment);
    }

    /**
     * 添加一个图片
     */
    public RichTextBuilder addImage(Drawable drawable, TextView textView) {
        if (drawable == null) return this;
        return addImage(drawable, textView, DynamicDrawableSpan.ALIGN_BASELINE);
    }

    /**
     * 添加一个图片
     */
    public RichTextBuilder addImage(Drawable drawable, int width, int height) {
        return addImage(drawable, width, height, DynamicDrawableSpan.ALIGN_BASELINE);
    }

    /**
     * 添加一个图片
     *
     * @param textView          以此TextView字体大小为准
     * @param verticalAlignment 选择{@link android.text.style.DynamicDrawableSpan#ALIGN_BASELINE}
     *                          或{@link android.text.style.DynamicDrawableSpan#ALIGN_BOTTOM}
     *                          或{@link android.text.style.DynamicDrawableSpan#ALIGN_CENTER}(api > Q)
     */
    public RichTextBuilder addImage(Drawable drawable, TextView textView, int verticalAlignment) {
        if (drawable == null) return this;
        int width, height;
        if (textView == null) {
            width = drawable.getIntrinsicWidth();
            height = drawable.getIntrinsicHeight();
        } else {
            width = height = (int) (textView.getTextSize() + 0.999F);
        }
        return addImage(drawable, width, height, verticalAlignment);
    }

    /**
     * 添加一个图片
     *
     * @param verticalAlignment 选择{@link android.text.style.DynamicDrawableSpan#ALIGN_BASELINE}
     *                          或{@link android.text.style.DynamicDrawableSpan#ALIGN_BOTTOM}
     *                          或{@link android.text.style.DynamicDrawableSpan#ALIGN_CENTER}(api > Q)
     */
    public RichTextBuilder addImage(Drawable drawable, int width, int height, int verticalAlignment) {
        if (drawable == null) return this;
        drawable.setBounds(0, 0, width, height);
        return addPart(" ", new ImageSpan(drawable, verticalAlignment));
    }

    /**
     * 添加一个文字，并指定多个span
     */
    public RichTextBuilder addPart(CharSequence text, Object... spans) {
        if (!TextUtils.isEmpty(text)) {
            if (spans != null && spans.length > 0) {
                final int start = mStringBuilder.length();
                final int end = start + text.length();
                mStringBuilder.append(text);
                for (Object span : spans) {
                    if (span != null) {
                        mStringBuilder.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            } else {
                mStringBuilder.append(text);
            }
        }
        return this;
    }

    /**
     * 对多个对象批量组合
     */
    public Texts batch() {
        return new Texts(this);
    }

    public NotEmptyTexts ifNotNone(CharSequence... texts) {
        return new NotEmptyTexts(this, texts);
    }

    /**
     * 对多个对象配置多种样式
     */
    public Styles styles() {
        return new Styles(this);
    }
}
