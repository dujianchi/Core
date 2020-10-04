package cn.dujc.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ItemDelete extends HorizontalScrollView {

    private static final Set<WeakReference<ItemDelete>> CACHED = new HashSet<>();

    public ItemDelete(Context context) {
        this(context, null, 0);
    }

    public ItemDelete(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemDelete(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static void removeFromCache(ItemDelete view) {
        final Iterator<WeakReference<ItemDelete>> iterator = CACHED.iterator();
        while (iterator.hasNext()) {
            final WeakReference<ItemDelete> next = iterator.next();
            if (next != null) {
                final ItemDelete cachedView = next.get();
                if (cachedView != null) {
                    if (cachedView == view || view == null) {
                        iterator.remove();
                        next.clear();
                    }
                }
            }
        }
    }

    public static void addToCache(ItemDelete view) {
        CACHED.add(new WeakReference<ItemDelete>(view));
    }

    public static void close(ItemDelete view) {
        for (final WeakReference<ItemDelete> reference : CACHED) {
            if (reference != null) {
                final ItemDelete get = reference.get();
                if (get != null && (get == view || view == null)) {
                    get.close();
                }
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        addToCache(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeFromCache(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final ViewGroup innerLayout = (ViewGroup) getChildAt(0);
        if (innerLayout != null) {
            final int width = MeasureSpec.getSize(widthMeasureSpec);
            final View firstChild = innerLayout.getChildAt(0);
            if (firstChild != null) {
                int innerLeft = getPaddingLeft()
                        //, innerTop = getPaddingTop()
                        , innerRight = getPaddingRight()
                        //, innerBottom = getPaddingBottom()
                        ;
                MarginLayoutParams layoutParams = (MarginLayoutParams) innerLayout.getLayoutParams();
                if (layoutParams != null) {
                    innerLeft += layoutParams.leftMargin;
                    //innerTop += layoutParams.topMargin;
                    innerRight += layoutParams.rightMargin;
                    //innerBottom += layoutParams.bottomMargin;
                }
                int childLeft = innerLayout.getPaddingLeft()//, childTop = innerLayout.getPaddingTop()
                        , childRight = innerLayout.getPaddingRight()
                        //, childBottom = innerLayout.getPaddingBottom()
                        ;
                MarginLayoutParams childParams = (MarginLayoutParams) firstChild.getLayoutParams();
                if (childParams != null) {
                    childLeft += childParams.leftMargin;
                    //childTop += childParams.topMargin;
                    childRight += childParams.rightMargin;
                    //childBottom += childParams.bottomMargin;
                }

                int innerLayoutWidth = width - innerLeft - innerRight;
                for (int index = 1; index < innerLayout.getChildCount(); index++) {
                    final View child = innerLayout.getChildAt(index);
                    if (child != null) innerLayoutWidth += child.getMeasuredWidth();
                }
                innerLayout.measure(MeasureSpec.makeMeasureSpec(innerLayoutWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
                firstChild.measure(MeasureSpec.makeMeasureSpec(width - innerLeft - childLeft - innerRight - childRight
                        , MeasureSpec.EXACTLY), heightMeasureSpec);
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final boolean result = super.onInterceptTouchEvent(ev);
        ItemDelete.close(null);
        return result;
    }

    public void open() {
        smoothScrollTo(getMeasuredWidth(), 0);
    }

    public void close() {
        smoothScrollTo(0, 0);
    }

}
