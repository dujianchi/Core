package cn.dujc.widget.fake;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import cn.dujc.widget.R;

/**
 * 伪造一个GridView，可以使用{@link BaseAdapter}，基本使用方法与{@link android.widget.GridView}差不多，但是实际上是一个相对布局RelativeLayout
 */
public class DuGridView extends RelativeLayout {

    private static final int LIMIT_MOVE_DP = 2;
    private static final int ID_START = R.id.du_custom_id_start;//id的前缀，因为id必须为正数，但是0不是正数
    private final DataSetObserver mDataSetObserver;
    private final float mLimitMove;//触摸位移的限制，超过此限制不再处理点击事件，定死值为 LIMIT_MOVE_DP dp
    private BaseAdapter mAdapter;

    private int mGridRows = 0, mGridColumn = 1;//行数、 列数
    private int mChildHeight = 0, mGridVerticalSpacing = 0, mGridHorizontalSpacing = 0;//子控件高度（配合heightStyle = exactValue）、垂直间距、水平间距
    private float mChildHeightWeight = 0;//子控件高度与宽度的比例
    private OnItemClickListener mOnItemClickListener;
    private float mDownX = 0, mDownY = 0;//触摸按下的位置，用于判断是否位移

    public DuGridView(Context context) {
        this(context, null, 0);
    }

    public DuGridView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DuGridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLimitMove = context.getResources().getDisplayMetrics().density * LIMIT_MOVE_DP;
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DuGridView);
            mGridRows = array.getInteger(R.styleable.DuGridView_du_grid_rows, mGridRows);
            mGridColumn = array.getInteger(R.styleable.DuGridView_du_grid_column, mGridColumn);

            mChildHeightWeight = array.getFloat(R.styleable.DuGridView_du_child_height_weight, mChildHeightWeight);

            mChildHeight = array.getDimensionPixelOffset(R.styleable.DuGridView_du_child_height, mChildHeight);
            mGridVerticalSpacing = array.getDimensionPixelOffset(R.styleable.DuGridView_du_grid_vertical_spacing, mGridVerticalSpacing);
            mGridHorizontalSpacing = array.getDimensionPixelOffset(R.styleable.DuGridView_du_grid_horizontal_spacing, mGridHorizontalSpacing);
            array.recycle();
        }

        if (mGridColumn < 1) {
            mGridColumn = 1;
        }

        mDataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                int width = getMeasuredWidth();
                if (width <= 0) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            resetViews(getMeasuredWidth());
                        }
                    });
                } else {
                    resetViews(width);
                }
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

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setAdapter(BaseAdapter adapter) {
        mAdapter = adapter;
        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(mDataSetObserver);
            mAdapter.notifyDataSetChanged();
        }
    }

    public BaseAdapter getAdapter() {
        return mAdapter;
    }

    public int getGridRows() {
        return mGridRows;
    }

    public void setGridRows(int gridRows) {
        mGridRows = gridRows;
    }

    public int getGridColumn() {
        return mGridColumn;
    }

    public void setGridColumn(int gridColumn) {
        mGridColumn = gridColumn;
    }

    public int getChildHeight() {
        return mChildHeight;
    }

    public void setChildHeight(int childHeight) {
        mChildHeight = childHeight;
    }

    public int getGridVerticalSpacing() {
        return mGridVerticalSpacing;
    }

    public void setGridVerticalSpacing(int gridVerticalSpacing) {
        mGridVerticalSpacing = gridVerticalSpacing;
    }

    public int getGridHorizontalSpacing() {
        return mGridHorizontalSpacing;
    }

    public void setGridHorizontalSpacing(int gridHorizontalSpacing) {
        mGridHorizontalSpacing = gridHorizontalSpacing;
    }

    public float getChildHeightWeight() {
        return mChildHeightWeight;
    }

    public void setChildHeightWeight(float childHeightWeight) {
        mChildHeightWeight = childHeightWeight;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    void resetViews(int width) {
        if (mAdapter != null) {
            final int count = mAdapter.getCount();
            while (getChildCount() > count) removeViewAt(getChildCount() - 1);

            final int childWidth = getOneChildWidth(width);
            final int childHeight = getOneChildHeightAsWidth(childWidth);
            for (int index = 0; index < count; index++) {
                View child = mAdapter.getView(index, getChildAt(index), this);

                ViewParent parent = child.getParent();
                if (parent instanceof ViewGroup) ((ViewGroup) parent).removeView(child);

                child.setId(ID_START + index);
                RelativeLayout.LayoutParams params = (LayoutParams) child.getLayoutParams();
                if (params == null) {
                    params = new LayoutParams(childWidth, childHeight);
                } else {
                    params.width = childWidth;
                    params.height = childHeight;
                }
                if (index % mGridColumn == 0) {
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
                    if (index == 0) {
                        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, TRUE);
                    } else {
                        params.topMargin = mGridVerticalSpacing;
                        params.addRule(BELOW, ID_START + index - mGridColumn);
                    }
                } else {
                    params.leftMargin = mGridHorizontalSpacing;
                    params.addRule(RelativeLayout.ALIGN_TOP, ID_START + index - 1);
                    params.addRule(RelativeLayout.RIGHT_OF, ID_START + index - 1);
                }
                addView(child, index, params);
            }
        }
    }

    private int getOneChildWidth(int parentWidth) {//获取一个子控件的宽度
        int width = (parentWidth - (mGridColumn - 1) * mGridHorizontalSpacing
                - getPaddingLeft() - getPaddingRight()) / mGridColumn;
        if (width == 0) width = LayoutParams.WRAP_CONTENT;
        return width;
    }

    private int getOneChildHeightAsWidth(int oneChildWidth) {//获取一个子控件的高度
        if (oneChildWidth == LayoutParams.WRAP_CONTENT) {
            return LayoutParams.WRAP_CONTENT;
        } else if (mChildHeightWeight != 0) {//高度比例 优先级最高
            return (int) (oneChildWidth * mChildHeightWeight);
        } else if (mChildHeight != 0) {//高度 优先级第二
            return mChildHeight;
        } else {//都没设置，则与宽度相等
            return oneChildWidth;
        }
    }
}
