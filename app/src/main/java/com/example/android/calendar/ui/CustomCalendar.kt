package com.example.android.calendar.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.android.calendar.R

class CustomCalendar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val eventCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var dayOfMonth: Int = 0
    private var hasEvent: Boolean = false
    private var isSelected: Boolean = false

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomCalendar)
        circlePaint.color = typedArray.getColor(
            R.styleable.CustomCalendar_circleColor,
            ContextCompat.getColor(context, android.R.color.transparent)
        )
        eventCirclePaint.color = typedArray.getColor(
            R.styleable.CustomCalendar_eventCircleColor,
            ContextCompat.getColor(context, android.R.color.transparent)
        )
        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cy = height / 2f
        val radius = width / 2f - PADDING

        circlePaint.style = Paint.Style.FILL
        circlePaint.color = if (isSelected) Color.RED else circlePaint.color
        canvas.drawCircle(cx, cy, radius, circlePaint)

        if (hasEvent) {
            val eventCircleRadius = radius / 4f
            eventCirclePaint.style = Paint.Style.FILL
            eventCirclePaint.color = Color.BLUE
            canvas.drawCircle(cx + radius / 2f, cy - radius / 2f, eventCircleRadius, eventCirclePaint)
        }
    }

    fun setDayOfMonth(dayOfMonth: Int) {
        this.dayOfMonth = dayOfMonth
        invalidate()
    }

    fun setHasEvent(hasEvent: Boolean) {
        this.hasEvent = hasEvent
        invalidate()
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        this.isSelected = selected
        invalidate()
    }

    companion object {
        private const val PADDING = 8f
    }
}
