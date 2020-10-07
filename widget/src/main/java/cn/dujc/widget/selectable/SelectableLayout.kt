package cn.dujc.widget.selectable

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Checkable
import androidx.constraintlayout.widget.ConstraintLayout
import cn.dujc.core.util.ViewUtils
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
                    operateChild(view, true)
                    if (!selectedViews.contains(view)) selectedViews.add(view)
                }
                max = max(ind, max)
            }
            removeOverflow()
            defaultInitialized = index >= max
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
        var result = false//super.dispatchTouchEvent(ev)
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
                if (ViewUtils.isViewUnderEvent(child, ev)) {
                    result = true
                    if (isChildSelected(child)) {
                        if (cancelable) {
                            operateChild(child, false)
                            selectedViews.remove(child)
                        }
                    } else {
                        if (!selectedViews.contains(child))
                            selectedViews.add(child)
                        operateChild(child, true)
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
     * 选中
     */
    fun select(vararg indexes: Int) {
        val iterator = selectedViews.iterator()
        while (iterator.hasNext()) {
            operateChild(iterator.next(), false)
            iterator.remove()
        }
        indexes?.forEach { ind ->
            getChildAt(ind)?.let { view ->
                operateChild(view, true)
                if (!selectedViews.contains(view)) selectedViews.add(view)
            }
        }
        removeOverflow()
    }

    private fun isChildSelected(child: View?): Boolean {
        if (child == null) return false
        when (child) {
            is Checkable -> return child.isChecked
            is ViewGroup -> {
                for (index in 0 until child.childCount) {
                    if (isChildSelected(child.getChildAt(index))) return true
                }
                return false
            }
            else -> return child.isSelected
        }
    }

    private fun operateChild(child: View?, selected: Boolean) {
        if (child == null) return
        when (child) {
            is Checkable -> {
                child.isChecked = selected
            }
            is ViewGroup -> {
                for (index in 0 until child.childCount) {
                    operateChild(child.getChildAt(index), selected)
                }
            }
            else -> {
                child.isSelected = selected
            }
        }
    }

    /**
     * 移除超出的
     */
    private fun removeOverflow() {
        val hash = selectedViews.hashCode()
        while (selectedViews.size > selectCount) {
            val remove = selectedViews.removeAt(0)
            //(next as? Checkable)?.isChecked = false
            operateChild(remove, false)
        }
        if (hash != selectedViews.hashCode()) {
            onSelectChangedListener?.onSelected(selectedViews, selectedIndexes(), selectedIndexLast())
        }
    }

}