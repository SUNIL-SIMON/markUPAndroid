package com.example.plansheets.Sheet

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import com.example.plansheets.Markers.ShapeMarkerView
import com.example.plansheets.R

class ControlBarView(private val sheetBaseViewInterface : SheetBaseViewInterface, context : Context) : ShapeMarkerView(sheetBaseViewInterface,context) {
    var controlBarView : View? = null
    fun getControlBarView(context: Context): View? {
        val convertView = LayoutInflater.from(context).inflate(R.layout.activity_main, null)
        val layout = convertView?.findViewById<FrameLayout>(R.id.sheetlayout)
        controlBarView = LayoutInflater.from(context).inflate(R.layout.controlbaritems, layout, false);
//        val markerLayersViewdrawable = ShapeDrawable()
//        markerLayersViewdrawable.shape = RectShape()
//        markerLayersViewdrawable.paint.color = Color.YELLOW
//        markerLayersViewdrawable.paint.strokeWidth = 10f
//        markerLayersViewdrawable.paint.style = Paint.Style.STROKE
//        controlBarView?.setBackground(markerLayersViewdrawable)


        return  controlBarView
    }
    fun setControlBarListners()
    {
        val btn = controlBarView?.findViewById<Button>(R.id.controlbarRect_button)
        btn?.setOnClickListener()
        {
            sheetBaseViewInterface.selectedMarkerToBeDroped(PLMShapeType.RECT)
        }
        val btn2 = controlBarView?.findViewById<Button>(R.id.controlbarEllipse_button)
        btn2?.setOnClickListener()
        {
            sheetBaseViewInterface.selectedMarkerToBeDroped(PLMShapeType.ROUND)
        }
        val btn3 = controlBarView?.findViewById<Button>(R.id.controlbarTriangle_button)
        btn3?.setOnClickListener()
        {
            sheetBaseViewInterface.selectedMarkerToBeDroped(PLMShapeType.TRIANGLE)
        }
        val btn4 = controlBarView?.findViewById<Button>(R.id.controlbarText_button)
        btn4?.setOnClickListener()
        {
            sheetBaseViewInterface.selectedMarkerToBeDroped(PLMShapeType.TEXT)
        }

        val btn5 = controlBarView?.findViewById<Button>(R.id.controlbarLine_button)
        btn5?.setOnClickListener()
        {
            sheetBaseViewInterface.selectedMarkerToBeDroped(PLMShapeType.LINE)
        }
        val btn6 = controlBarView?.findViewById<Button>(R.id.controlbarPen_button)
        btn6?.setOnClickListener()
        {
            sheetBaseViewInterface.selectedMarkerToBeDroped(PLMShapeType.PATH)
        }
        val btn7 = controlBarView?.findViewById<Button>(R.id.controlbarHighlighter_button)
        btn7?.setOnClickListener()
        {
            sheetBaseViewInterface.selectedMarkerToBeDroped(PLMShapeType.HIGHLIGHTER)
        }
        val btn8 = controlBarView?.findViewById<Button>(R.id.controlbarImage_button)
        btn8?.setOnClickListener()
        {
            sheetBaseViewInterface.selectedMarkerToBeDroped(PLMShapeType.IMAGE)
        }
    }
    open fun performOrientation(screenWidth:Int,screenHeight : Int)
    {
        controlBarView?.y = screenHeight.toFloat() - 200
    }
}
