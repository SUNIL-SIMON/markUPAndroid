
package com.example.plansheets.Markers.LineMarkers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.FrameLayout
import com.example.plansheets.Markers.ShapeMarkerView
import com.example.plansheets.Sheet.MarkerMenuOptionsType
import com.example.plansheets.Sheet.MarkerMenuType
import com.example.plansheets.Sheet.SheetBaseViewInterface
import java.util.*

class LineMarkerView(private val sheetBaseViewInterface : SheetBaseViewInterface, context : Context) : ShapeMarkerView(sheetBaseViewInterface,context) {
    val roundView = ImageView(context)


    override fun getShapeMarker(context: Context): View?
    {
        shapeMarkerView.isEnabled = false
        roundView.isEnabled = false
        shapeMarkerView.isClickable = false
        roundView.isClickable = false

        shapeMarkerView.addView(roundView)
        roundView.x = 0f
        roundView.y = 0f

        roundView.setLayoutParams(FrameLayout.LayoutParams(shapeMarkerView.layoutParams.width , shapeMarkerView.layoutParams.height ))
        addLineHolders(context)
        return shapeMarkerView
    }
    fun setCanvas()
    {
        val bitmap = Bitmap.createBitmap(roundView.layoutParams.width, roundView.layoutParams.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = Color.GREEN
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4F
        canvas.drawLine(point1Holder.x + (holderSize/2),point1Holder.y + (holderSize/2),point2Holder.x + (holderSize/2),point2Holder.y + (holderSize/2),paint)
        roundView.setImageBitmap(bitmap)
    }
    override fun holderMoved()
    {
        setCanvas()
        super.holderMoved()
    }
    override fun markerClicked()
    {
        showMenu(shapeMarkerView)
    }
    override fun hideMenu()
    {

    }
    override fun reconfigureRect()
    {
        roundView.setLayoutParams(FrameLayout.LayoutParams(shapeMarkerView.layoutParams.width , shapeMarkerView.layoutParams.height ))
        setCanvas()
    }
    private fun showMenu(view: View) {
        var list :ArrayList<MarkerMenuOptionsType> = ArrayList()
        list.add(MarkerMenuOptionsType(MarkerMenuType.DELETE,"Delete"))
        list.add(MarkerMenuOptionsType(MarkerMenuType.COPY,"Copy"))
        list.add(MarkerMenuOptionsType(MarkerMenuType.PASTE,"Paste"))
        list.add(MarkerMenuOptionsType(MarkerMenuType.INFO,"Info"))

        openMenuOptions(list)
    }
}
