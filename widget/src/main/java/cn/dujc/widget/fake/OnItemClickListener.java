package cn.dujc.widget.fake;

import android.view.View;
import android.view.ViewGroup;

/**
 * {@link DuGridView}和{@link DuListView}的点击事件
 */
public interface OnItemClickListener {
    void onItemClick(ViewGroup parent, int index, View child);
}
