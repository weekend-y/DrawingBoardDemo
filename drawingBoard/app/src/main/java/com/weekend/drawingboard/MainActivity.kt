package com.weekend.drawingboard

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
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

        val seekBarBrushSize: SeekBar = findViewById(R.id.seekBarBrushSize)
        seekBarBrushSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                drawingView.brushSize = progress.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val btnColorPicker: ImageButton = findViewById(R.id.btnColorPicker)
        btnColorPicker.setOnClickListener {
            showColorPickerDialog()
        }
    }

    private fun showColorPickerDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_color_picker)

        val colorView = dialog.findViewById<android.widget.TextView>(R.id.colorView)
        val colorPickerView = dialog.findViewById<android.widget.SeekBar>(R.id.colorPickerView)
        val colorPickerView2 = dialog.findViewById<android.widget.SeekBar>(R.id.colorPickerView2)
        val colorPickerView3 = dialog.findViewById<android.widget.SeekBar>(R.id.colorPickerView3)

        colorPickerView.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val r = progress
                val g = colorPickerView2.progress
                val b = colorPickerView3.progress
                val color = Color.rgb(r, g, b)
                colorView.setBackgroundColor(color)
                drawingView.brushColor = color
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        colorPickerView2.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val r = colorPickerView.progress
                val g = progress
                val b = colorPickerView3.progress
                val color = Color.rgb(r, g, b)
                colorView.setBackgroundColor(color)
                drawingView.brushColor = color
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        colorPickerView3.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val r = colorPickerView.progress
                val g = colorPickerView2.progress
                val b = progress
                val color = Color.rgb(r, g, b)
                colorView.setBackgroundColor(color)
                drawingView.brushColor = color
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        dialog.show()
    }

    fun showZoomDialog(view: View) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_zoom)

        val seekBarZoom = dialog.findViewById<SeekBar>(R.id.seekBarZoom)
        seekBarZoom.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val scale = progress / 100f
                drawingView.scaleX = scale
                drawingView.scaleY = scale
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        dialog.show()
    }
}