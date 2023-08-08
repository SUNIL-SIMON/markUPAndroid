package com.example.plansheets.Sheet

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import com.example.plansheets.Markers.ShapeMarkerView

class ControlBarView(private val sheetBaseViewInterface : SheetBaseViewInterface, context : Context) : ShapeMarkerView(sheetBaseViewInterface,context) {
    val controlBarView = FrameLayout(context)
    fun getControlBarView(context: Context): View? {

        val btn = Button(context)
        btn.text = "Rect"
        controlBarView.addView(btn)
        btn.setLayoutParams(FrameLayout.LayoutParams(100, 60))
        btn.x = 210f
        btn.y = 0f
        btn.setOnClickListener()
        {
            sheetBaseViewInterface.selectedMarkerToBeDroped(PLMShapeType.RECT)
        }
        val btn2 = Button(context)
        btn2.text = "Ellipse"
        controlBarView.addView(btn2)
        btn2.setLayoutParams(FrameLayout.LayoutParams(100, 60))
        btn2.x = 320f
        btn2.y = 0f
        btn2.setOnClickListener()
        {
            sheetBaseViewInterface.selectedMarkerToBeDroped(PLMShapeType.ROUND)
        }
        val btn3 = Button(context)
        btn3.text = "Triangle"
        controlBarView.addView(btn3)
        btn3.setLayoutParams(FrameLayout.LayoutParams(100, 60))
        btn3.x = 430f
        btn3.y = 0f
        btn3.setOnClickListener()
        {
            sheetBaseViewInterface.selectedMarkerToBeDroped(PLMShapeType.TRIANGLE)
        }
        val btn4 = Button(context)
        btn4.text = "Line"
        controlBarView.addView(btn4)
        btn4.setLayoutParams(FrameLayout.LayoutParams(100, 60))
        btn4.x = 540f
        btn4.y = 0f
        btn4.setOnClickListener()
        {
            sheetBaseViewInterface.selectedMarkerToBeDroped(PLMShapeType.LINE)
        }

        val btn5 = Button(context)
        btn5.text = "Pen"
        controlBarView.addView(btn5)
        btn5.setLayoutParams(FrameLayout.LayoutParams(100, 60))
        btn5.x = 650f
        btn5.y = 0f
        btn5.setOnClickListener()
        {
            sheetBaseViewInterface.selectedMarkerToBeDroped(PLMShapeType.PATH)
        }

        val controlBarViewdrawable = ShapeDrawable()
        controlBarViewdrawable.shape = RectShape()
        controlBarViewdrawable.paint.color = Color.BLUE
        controlBarViewdrawable.paint.strokeWidth = 2f
        controlBarViewdrawable.paint.style = Paint.Style.STROKE
        controlBarView.setBackground(controlBarViewdrawable)
        return controlBarView
    }
    open fun performOrientation(screenWidth:Int,screenHeight : Int)
    {
        controlBarView.x = 0f
        controlBarView.y = screenHeight.toFloat() - 200
        controlBarView.setLayoutParams(FrameLayout.LayoutParams(screenWidth, 60))
    }
}
