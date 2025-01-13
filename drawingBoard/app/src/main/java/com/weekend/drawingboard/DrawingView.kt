package com.weekend.drawingboard

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var currentPath: Path? = null
    private var currentPaint: Paint? = null
    private val paths = mutableListOf<Pair<Path, Paint>>()
    private val undonePaths = mutableListOf<Pair<Path, Paint>>()

    private val defaultPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 10f
        isAntiAlias = true // 抗锯齿
    }

    var brushSize: Float
        get() = defaultPaint.strokeWidth
        set(value) {
            defaultPaint.strokeWidth = value
            currentPaint?.strokeWidth = value
        }

    var brushColor: Int
        get() = defaultPaint.color
        set(value) {
            defaultPaint.color = value
            currentPaint?.color = value
        }

    var isEraserMode = false
        set(value) {
            field = value
            if (value) {
                defaultPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                currentPaint?.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            } else {
                defaultPaint.xfermode = null
                currentPaint?.xfermode = null
            }
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paths.forEach { (path, paint) -> canvas.drawPath(path, paint) }
        currentPath?.let { canvas.drawPath(it, currentPaint ?: defaultPaint) }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentPath = Path()
                currentPath?.moveTo(x, y)
                currentPaint = Paint(defaultPaint)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                currentPath?.lineTo(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                currentPath?.let { path ->
                    currentPaint?.let { paint ->
                        paths.add(Pair(path, paint))
                    }
                }
                currentPath = null
                currentPaint = null
            }
        }
        return true
    }

    fun undo() {
        if (paths.isNotEmpty()) {
            undonePaths.add(paths.removeLast())
            invalidate()
        }
    }

    fun redo() {
        if (undonePaths.isNotEmpty()) {
            paths.add(undonePaths.removeLast())
            invalidate()
        }
    }
}