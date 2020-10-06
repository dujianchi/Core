package cn.dujc.widget.selectable

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import cn.dujc.widget.R

/**
 * 可以选择的控件
 */
class SelectableView(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
    : FrameLayout(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var layoutId: Int = R.layout.widget_default_selectable_view

    private var iconView: ImageView? = null
    private var textView: TextView? = null

    init {
        attrs?.let {
            val array = context.obtainStyledAttributes(it, R.styleable.SelectableView)
            layoutId = array.getInt(R.styleable.SelectableView_du_selectableViewLayoutId, layoutId)

            val layout = LayoutInflater.from(context).inflate(layoutId, this)
            iconView = (layout.findViewById(R.id.du_selectableViewIconId) as? ImageView)
            iconView?.let { icon ->
                val iconWidth = array.getDimensionPixelOffset(R.styleable.SelectableView_du_selectableViewIconWidth, 0)
                val iconHeight = array.getDimensionPixelOffset(R.styleable.SelectableView_du_selectableViewIconHeight, 0)
                val iconTextSpacing = array.getDimensionPixelOffset(R.styleable.SelectableView_du_selectableViewIconTextSpacing, -1)
                val drawable: Drawable? = array.getDrawable(R.styleable.SelectableView_du_selectableViewIconSrc)
                val layoutParams = icon.layoutParams as? MarginLayoutParams
                        ?: MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                if (iconWidth != 0) layoutParams.width = iconWidth
                if (iconHeight != 0) layoutParams.height = iconHeight
                if (iconTextSpacing >= 0) layoutParams.bottomMargin = iconTextSpacing
                if (drawable != null) icon.setImageDrawable(drawable)
                icon.layoutParams = layoutParams
            }

            textView = (layout.findViewById(R.id.du_selectableViewTextId) as? TextView)
            textView?.let { text ->
                val textSize = array.getDimensionPixelOffset(R.styleable.SelectableView_du_selectableViewTextSize, 0)
                val textColor = array.getColorStateList(R.styleable.SelectableView_du_selectableViewTextColor)
                val textStr = array.getString(R.styleable.SelectableView_du_selectableViewText)
                if (textSize != 0) text.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
                if (textColor != null) text.setTextColor(textColor)
                text.text = textStr
            }
            array.recycle()
        }
    }


}