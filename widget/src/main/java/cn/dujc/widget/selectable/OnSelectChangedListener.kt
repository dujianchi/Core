package cn.dujc.widget.selectable

import android.view.View

public interface OnSelectChangedListener {

    public fun onSelected(selectedViews: List<View>, selectedIndexes: Array<Int>, lastIndex: Int)

}