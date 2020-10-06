package cn.dujc.widget.selectable

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import cn.dujc.widget.R
import kotlin.math.max

/**
 * 可以选择的布局
 */
class SelectableLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
        ConstraintLayout(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var selectCount: Int = 1
        get() = field
        set(value) {
            removeOverflow()
            field = value
        }
    private var defaultSelectedIndexes: Array<Int>? = null
        get() = field
        set(value) {
            removeOverflow()
            field = value
        }
    private var defaultInitialized = false
    private var cancelable = false
    private val selectedViews = ArrayList<View>()
    var onSelectChangedListener: OnSelectChangedListener? = null

    init {
        attrs?.let {
            val array = context.obtainStyledAttributes(it, R.styleable.SelectableLayout)
            selectCount = array.getInt(R.styleable.SelectableLayout_du_selectableLayoutSelectCount, selectCount)
            defaultSelectedIndexes =
                    array.getString(R.styleable.SelectableLayout_du_selectableLayoutSelectedIndexes)?.split(",")
                            ?.map { ch -> ch.toIntOrNull() ?: -1 }?.toTypedArray()
            cancelable = array.getBoolean(R.styleable.SelectableLayout_du_selectableLayoutCancelable, cancelable)
            array.recycle()
        }
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        super.addView(child, index, params)
        if (!defaultInitialized) {
            var max = -1
            defaultSelectedIndexes?.forEach { ind ->
                getChildAt(ind)?.let { view ->
                    view.isSelected = true
                    if (!selectedViews.contains(view)) selectedViews.add(view)
                    view
                }
                max = max(ind, max)
            }
            defaultInitialized = index >= max
            removeOverflow()
        }
    }

    override fun removeView(view: View?) {
        super.removeView(view)
        selectedViews.remove(view)
        defaultSelectedIndexes?.let { indexes ->
            var max = -1
            indexes.forEach { i -> max = max(max, i) }
            defaultInitialized = childCount < max
        }
        removeOverflow()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val result = super.dispatchTouchEvent(ev)
        val action = ev.action
        if (
        /*action == MotionEvent.ACTION_UP
        || action == MotionEvent.ACTION_POINTER_UP
        || action == MotionEvent.ACTION_CANCEL
        || action == MotionEvent.ACTION_BUTTON_RELEASE
        || action == MotionEvent.ACTION_HOVER_EXIT
        || action == MotionEvent.ACTION_OUTSIDE*/
        // ⬆抬起  ⬇按下 （模拟器上按下比较有效，但个人更喜欢抬起）
                action == MotionEvent.ACTION_DOWN
                || action == MotionEvent.ACTION_POINTER_DOWN
                || action == MotionEvent.ACTION_BUTTON_PRESS
        ) {
            for (index in 0 until childCount) {
                val child = getChildAt(index)
                if (isViewUnderEvent(child, ev)) {
                    if (child.isSelected) {
                        if (cancelable) {
                            child.isSelected = false
                            selectedViews.remove(child)
                        }
                    } else {
                        if (!selectedViews.contains(child))
                            selectedViews.add(child)
                        child.isSelected = true
                    }
                    /*(child as? Checkable)?.let { checkable ->
                        checkable.isChecked = !checkable.isChecked
                        if (checkable.isChecked) selectedViews.add(child)
                    }*/
                }
            }
            removeOverflow()
        }
        return result
    }

    /**
     * 选中的index组合
     */
    fun selectedIndexes(): Array<Int> = selectedViews.map { view -> indexOfChild(view) }.toTypedArray()

    /**
     * 选中的index最后的那个，没有选中则返回-1
     */
    fun selectedIndexLast(): Int = selectedViews.getOrNull(selectedViews.size - 1)?.let { indexOfChild(it) }
            ?: -1

    /**
     * 移除超出的
     */
    private fun removeOverflow() {
        while (selectedViews.size > selectCount) {
            val remove = selectedViews.removeAt(0)
            //(next as? Checkable)?.isChecked = false
            remove.isSelected = false
        }
        onSelectChangedListener?.onSelected(selectedViews, selectedIndexes(), selectedIndexLast())
    }

    private fun isViewUnderEvent(view: View, event: MotionEvent): Boolean {
        val xy = IntArray(2)
        view.getLocationOnScreen(xy)
        val width = view.width
        val height = view.height
        val x = event.rawX
        val y = event.rawY
        return !(x < xy[0] || y < xy[1] || x > xy[0] + width || y > xy[1] + height)
    }
}