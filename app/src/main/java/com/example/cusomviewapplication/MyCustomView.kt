package com.example.cusomviewapplication

import android.animation.Animator.AnimatorListener
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import androidx.core.content.withStyledAttributes
import kotlin.math.ceil
import kotlin.math.min
import kotlin.random.Random

class MyCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttrs, defStyleRes) {
    private var radius = 0F
    private var center = PointF(0F, 0F)
    private var oval = RectF(0F, 0F, 0F, 0F)

    private var lineWidth = context.convertDpToPx(5F).toFloat()
    private var fontSize = context.convertDpToPx(40F).toFloat()
    private var colors = emptyList<Int>()
    var progress = 0F
        set(value) {
            field =value
            invalidate()
        }
    var angleOffset= 0F
        set(value) {
            field =value
            invalidate()
        }
    private var valueAnimator: ValueAnimator? = null

    init {
        context.withStyledAttributes(attrs, R.styleable.MyCustomView) {
            lineWidth = getDimension(R.styleable.MyCustomView_lineWidth, lineWidth)
            fontSize = getDimension(R.styleable.MyCustomView_fontSize, fontSize)
            val resId = getResourceId(R.styleable.MyCustomView_colors, 0)
            colors = resources.getIntArray(resId).toList()
        }
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = lineWidth
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.BEVEL
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = fontSize
    }
    var cost: Float = 0F
    var data: List<Float> = emptyList()
        set(value) {
            field = value
            update()
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = min(w, h) / 2F - lineWidth / 2
        center = PointF(w / 2F, h / 2F)
        oval = RectF(
            center.x - radius, center.y - radius,
            center.x + radius, center.y + radius,
        )
    }

    override fun onDraw(canvas: Canvas) {
        if (data.isEmpty() || cost == 0F) {
            return
        }
        var startFrom = -90F + angleOffset
        for ((index, datum) in data.withIndex()) {
            val angle = 360F * datum / cost
            paint.color = colors.getOrNull(index) ?: randomColor()
            canvas.drawArc(oval, startFrom, angle* progress, false, paint)
            startFrom += angle
        }
        paint.color = colors.getOrNull(0) ?: randomColor()
        canvas.drawArc(oval, -90F + angleOffset, progress * 180F * data[0] / data.sum(), false, paint)

        canvas.drawText(
            "%.2f%%".format(data.fold(0F) { sumProportion, value ->
                sumProportion + (value / cost)
            } * 100),
            center.x,
            center.y + textPaint.textSize / 4,
            textPaint,
        )
    }
    private fun update() {
        ObjectAnimator.ofPropertyValuesHolder(this, PropertyValuesHolder.ofFloat("progress",0F,1F), PropertyValuesHolder.ofFloat("angleOffset",0F,360F))
           .apply {
                duration = 2000
                interpolator = LinearInterpolator()
            }.start()
    }
    private fun randomColor() = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt())
}

fun Context.convertDpToPx(dp: Float): Int =
    ceil(this.resources.displayMetrics.density * dp).toInt()