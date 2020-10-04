package cn.dujc.widget.fake;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * 伪造一个ListView，可以使用{@link BaseAdapter}，基本使用方法与{@link android.widget.ListView}差不多，但是实际上是一个线性布局LinearLayout
 */
public class DuListView extends LinearLayout {

    private static final int LIMIT_MOVE_DP = 2;
    private final DataSetObserver mDataSetObserver;
    private final float mLimitMove;//触摸位移的限制，超过此限制不再处理点击事件，定死值为 LIMIT_MOVE_DP dp

    private BaseAdapter mAdapter;
    private OnItemClickListener mOnItemClickListener;
    private float mDownX = 0, mDownY = 0;//触摸按下的位置，用于判断是否位移

    public DuListView(Context context) {
        this(context, null, 0);
    }

    public DuListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DuListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLimitMove = context.getResources().getDisplayMetrics().density * LIMIT_MOVE_DP;
        mDataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                resetViews();
            }
        };
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int childCount;
        if (mOnItemClickListener != null && (childCount = getChildCount()) > 0) {
            final int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP) {
                final float x = ev.getRawX(), y = ev.getRawY();
                if (Math.abs(x - mDownX) > mLimitMove || Math.abs(y - mDownY) > mLimitMove) {
                    return super.dispatchTouchEvent(ev);
                }
                boolean clicked = false;
                for (int index = 0; index < childCount; index++) {
                    View child = getChildAt(index);
                    if (child != null) {
                        final int[] location = new int[2];
                        child.getLocationOnScreen(location);
                        if (location[0] <= x
                                && x <= location[0] + child.getWidth()
                                && location[1] <= y
                                && y <= location[1] + child.getHeight()) {
                            mOnItemClickListener.onItemClick(this, index, child);
                            clicked = true;
                            break;
                        }
                    }
                }
                if (!clicked) performClick();
            } else if (action == MotionEvent.ACTION_DOWN) {
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public BaseAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(BaseAdapter adapter) {
        mAdapter = adapter;
        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(mDataSetObserver);
            mAdapter.notifyDataSetChanged();
        }
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    void resetViews() {
        if (mAdapter != null) {
            final int count = mAdapter.getCount();
            while (getChildCount() > count) removeViewAt(getChildCount() - 1);

            for (int index = 0; index < count; index++) {
                View child = mAdapter.getView(index, getChildAt(index), this);
                ViewParent parent = child.getParent();
                if (parent instanceof ViewGroup) ((ViewGroup) parent).removeView(child);
                addView(child, index);
            }
        }
    }
}
