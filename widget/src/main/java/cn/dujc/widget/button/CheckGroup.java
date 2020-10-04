package cn.dujc.widget.button;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.Iterator;
import java.util.Stack;

import cn.dujc.core.util.ViewUtils;

public class CheckGroup extends LinearLayout {

    private final Stack<Checkable> mCheckables = new Stack<>(), mCheckeds = new Stack<>();
    private int mCheckCount = 1;

    public CheckGroup(Context context) {
        this(context, null, 0);
    }

    public CheckGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        calcAfterAdd(child);
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        calcAfterRemove(view);
    }

    @Override
    public void removeViewAt(int index) {
        super.removeViewAt(index);
        calcAfterRemove(getChildAt(index));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean result = super.dispatchTouchEvent(ev);
        final int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP
                || action == MotionEvent.ACTION_POINTER_UP
                || action == MotionEvent.ACTION_CANCEL
        ) {
            for (Checkable checkable : mCheckables) {
                if (checkable instanceof View) {
                    if (ViewUtils.isViewUnderEvent((View) checkable, ev)) {
                        if (checkable.isChecked()) {
                            mCheckeds.add(checkable);
                        } else {
                            mCheckeds.remove(checkable);
                        }
                        calcAllCheckeds();
                    }
                }
            }
        }
        return result;
    }

    public void setCheckCount(int checkCount) {
        int checkedSize;
        if (checkCount != mCheckCount && (checkedSize = mCheckeds.size()) > checkCount) {
            for (Checkable next : mCheckables) {
                if (checkedSize <= checkCount) break;
                if (next.isChecked()) {
                    checkedSize--;
                    next.setChecked(false);
                    mCheckeds.remove(next);
                }
            }
        }
        mCheckCount = checkCount;
    }

    private void calcAllCheckeds() {
        /*mCheckeds.clear();
        for (Checkable next : mCheckables) {
            if (next.isChecked()) {
                mCheckeds.add(next);
            }
        }*/
        int checkedSize = mCheckeds.size();
        if (checkedSize > mCheckCount) {
            Iterator<Checkable> iterator = mCheckeds.iterator();
            while (iterator.hasNext()) {
                if (checkedSize <= mCheckCount) break;
                Checkable next = iterator.next();
                if (next.isChecked()) {
                    checkedSize--;
                    next.setChecked(false);
                    iterator.remove();
                }
            }
        }
    }

    private void calcAfterRemove(View view) {
        if (view instanceof Checkable) {
            final Checkable checkable = (Checkable) view;
            mCheckables.remove(checkable);
            if (checkable.isChecked()) mCheckeds.remove(checkable);
        } else if (view instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup) view;
            for (int index = 0, count = viewGroup.getChildCount(); index < count; index++) {
                final View child = viewGroup.getChildAt(index);
                calcAfterRemove(child);
            }
        }
    }

    private void calcAfterAdd(View view) {
        if (view instanceof Checkable) {
            final Checkable checkable = (Checkable) view;
            mCheckables.add(checkable);
            if (checkable.isChecked()) mCheckeds.add(checkable);
        } else if (view instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup) view;
            for (int index = 0, count = viewGroup.getChildCount(); index < count; index++) {
                final View child = viewGroup.getChildAt(index);
                calcAfterAdd(child);
            }
        }
    }

}
