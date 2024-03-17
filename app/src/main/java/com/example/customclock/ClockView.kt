package com.example.customclock

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class ClockView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width
        val height = height
        val radius = if (width > height) height / 2f else width / 2f
        val cx = width / 2f
        val cy = height / 2f

        paint.reset()
        paint.color = 0xFF000000.toInt()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 8f
        canvas.drawCircle(cx, cy, radius, paint)

        paint.textSize = 60f
        for (i in 1..12) {
            paint.style = Paint.Style.FILL
            val angle = Math.PI / 6 * (i - 3)
            val text = i.toString()
            val textWidth = paint.measureText(text)
            val textHeight = paint.descent() - paint.ascent()
            val x = (cx + radius * 0.8 * cos(angle) - textWidth / 2).toFloat()
            val y = (cy + radius * 0.8 * sin(angle) + textHeight / 4).toFloat()
            canvas.drawText(text, x, y, paint)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 4f
            val dotX = (cx + radius * 0.92 * cos(angle)).toFloat()
            val dotY = (cy + radius * 0.92 * sin(angle)).toFloat()
            canvas.drawCircle(dotX, dotY, 2f, paint)
        }

        paint.strokeWidth = 2f
        for (j in 1..60) {
            if (j % 5 != 0) {
                val dotAngle = Math.PI / 30 * (j - 15)
                val dotX = (cx + radius * 0.92 * cos(dotAngle)).toFloat()
                val dotY = (cy + radius * 0.92 * sin(dotAngle)).toFloat()
                canvas.drawCircle(dotX, dotY, 2f, paint)
            }
        }

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        paint.color = 0xFF000000.toInt()
        paint.style = Paint.Style.FILL
        paint.strokeCap = Paint.Cap.ROUND
        val rect = RectF(cx - radius / 20, cy - radius * 0.5f, cx + radius / 20, cy + radius * 0.2f)
        canvas.save()
        canvas.rotate((hour + minute / 60f) * 360 / 12, cx, cy)
        canvas.drawRect(rect, paint)
        canvas.restore()

        rect.set(cx - radius / 40, cy - radius * 0.8f, cx + radius / 40, cy + radius * 0.2f)
        canvas.save()
        canvas.rotate((minute + second / 60f) * 360 / 60, cx, cy)
        canvas.drawRect(rect, paint)
        canvas.restore()

        paint.color = 0xFFFF0000.toInt()
        rect.set(cx - radius / 100, cy - radius * 0.9f, cx + radius / 100, cy + radius * 0.1f)
        canvas.save()
        canvas.rotate(second * 360 / 60f, cx, cy)
        canvas.drawRect(rect, paint)
        canvas.restore()

        postInvalidateDelayed(1000)
    }
}
