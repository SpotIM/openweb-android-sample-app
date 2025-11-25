package openweb.sample.ui.screens.settings.customtheme

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import openweb.sample.R

class CustomColorCheckBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var isChecked = false
    private var innerBoxColor: Int = Color.GREEN
    private var checkBoxText: String = "Custom CheckBox"
    private var textColor: Int = R.color.defaultTextColor

    private val checkBox: View
    private val textView: TextView

    private val xPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        strokeWidth = 2.toDp().toFloat()
        style = Paint.Style.STROKE
    }

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

        context.theme.obtainStyledAttributes(attrs, R.styleable.CustomColorCheckBox, 0, 0).apply {
            try {
                checkBoxText = getString(R.styleable.CustomColorCheckBox_checkBoxText) ?: checkBoxText
                innerBoxColor = getColor(R.styleable.CustomColorCheckBox_checkBoxInnerColor, innerBoxColor)
                textColor = getColor(
                    R.styleable.CustomColorCheckBox_checkBoxTextColor,
                    resources.getColor(textColor, null)
                )
            } finally {
                recycle()
            }
        }

        checkBox = object : View(context) {
            override fun onDraw(canvas: Canvas) {
                super.onDraw(canvas)
                if (isChecked) {
                    // Draw the inner box with the custom color
                    setBackgroundColor(innerBoxColor)
                } else {
                    // Draw the red "X" inside the box
                    setBackgroundColor(Color.WHITE)
                    val padding = 0F
                    canvas.drawLine(padding, padding, width - padding, height - padding, xPaint)
                    canvas.drawLine(padding, height - padding, width - padding, padding, xPaint)
                }
            }
        }.apply {
            layoutParams = LayoutParams(16.toDp(), 16.toDp())
        }

        textView = TextView(context).apply {
            text = checkBoxText
            setTextColor(textColor)
            setPadding(4.toDp(), 0, 0, 0)
        }

        addView(checkBox)
        addView(textView)

        updateView()
    }

    fun toggle() {
        isChecked = !isChecked
        updateView()
    }

    private fun updateView() {
        checkBox.invalidate() // Redraw the view with updated state
        invalidate()
    }

    fun setInnerBoxColor(color: Int) {
        innerBoxColor = color
        if (isChecked) {
            updateView()
        }
    }

    fun setCheckBoxText(text: String) {
        checkBoxText = text
        textView.text = checkBoxText
    }

    fun setTextColor(color: Int) {
        textColor = color
        textView.setTextColor(textColor)
    }

    fun setChecked(checked: Boolean, color: Int? = null) {
        isChecked = checked
        if (color != null) setInnerBoxColor(color)
        updateView()
    }

    private fun Int.toDp(): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}
