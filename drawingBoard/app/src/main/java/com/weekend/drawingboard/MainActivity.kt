package com.weekend.drawingboard

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var drawingView: DrawingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView = findViewById(R.id.drawingView)

        val undoButton: Button = findViewById(R.id.btnUndo)
        undoButton.setOnClickListener {
            drawingView.undo()
        }

        val eraserButton: Button = findViewById(R.id.btnEraser)
        eraserButton.setOnClickListener {
            drawingView.isEraserMode = !drawingView.isEraserMode
            eraserButton.text = if (drawingView.isEraserMode) "画笔" else "橡皮擦"
        }

        val btnBrushSize: Button = findViewById(R.id.btnBrushSize)
        btnBrushSize.setOnClickListener {
            showBrushSizeDialog()
        }

        val btnColorPicker: ImageButton = findViewById(R.id.btnColorPicker)
        btnColorPicker.setOnClickListener {
            showColorPickerDialog()
        }

    }

    private fun showBrushSizeDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_brush_size)

        val brushSizes = listOf(5f, 10f, 15f, 20f, 25f)
        val container = dialog.findViewById<LinearLayout>(R.id.brushSizeContainer)

        brushSizes.forEach { size ->
            val button = Button(this).apply {
                text = "$size"
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                setOnClickListener {
                    drawingView.brushSize = size
                    dialog.dismiss()
                }
            }
            container.addView(button)
        }

        dialog.show()
    }

    private fun showColorPickerDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_color_picker)

        val colorView = dialog.findViewById<android.widget.TextView>(R.id.colorView)
        val colorPickerView = dialog.findViewById<android.widget.SeekBar>(R.id.colorPickerView)
        val colorPickerView2 = dialog.findViewById<android.widget.SeekBar>(R.id.colorPickerView2)
        val colorPickerView3 = dialog.findViewById<android.widget.SeekBar>(R.id.colorPickerView3)

        colorPickerView.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                val r = progress
                val g = colorPickerView2.progress
                val b = colorPickerView3.progress
                val color = android.graphics.Color.rgb(r, g, b)
                colorView.setBackgroundColor(color)
                drawingView.brushColor = color
            }

            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })

        colorPickerView2.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                val r = colorPickerView.progress
                val g = progress
                val b = colorPickerView3.progress
                val color = android.graphics.Color.rgb(r, g, b)
                colorView.setBackgroundColor(color)
                drawingView.brushColor = color
            }

            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })

        colorPickerView3.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                val r = colorPickerView.progress
                val g = colorPickerView2.progress
                val b = progress
                val color = android.graphics.Color.rgb(r, g, b)
                colorView.setBackgroundColor(color)
                drawingView.brushColor = color
            }

            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })

        dialog.show()
    }
}