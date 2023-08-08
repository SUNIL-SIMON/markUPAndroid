package com.example.plansheets.Markers.ShapeMarkers

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

class TriangleMarkerView(private val sheetBaseViewInterface : SheetBaseViewInterface, context : Context) : ShapeMarkerView(sheetBaseViewInterface,context) {
    val roundView = ImageView(context)



    override fun getShapeMarker(context: Context): View?
    {
        shapeMarkerView.addView(roundView)
        roundView.setLayoutParams(FrameLayout.LayoutParams(shapeDefaultSize - holderSize, shapeDefaultSize - holderSize))
        roundView.x = (holderSize/2).toFloat()
        roundView.y = (holderSize/2).toFloat()

        super.getShapeMarker(context)
        addShapeHolders(context)
        setCanvas()

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
        canvas.drawLine((roundView.layoutParams.width/2).toFloat(),0f,roundView.layoutParams.width.toFloat(),roundView.layoutParams.height.toFloat(),paint)
        canvas.drawLine(roundView.layoutParams.width.toFloat(),roundView.layoutParams.height.toFloat(),0f,roundView.layoutParams.height.toFloat(),paint)
        canvas.drawLine(0f,roundView.layoutParams.height.toFloat(),(roundView.layoutParams.width/2).toFloat(),0f,paint)
        roundView.setImageBitmap(bitmap)
    }
    override fun holderMoved()
    {
        reconfigureRect()
        super.holderMoved()
    }
    override fun reconfigureRect()
    {
        roundView.setLayoutParams(FrameLayout.LayoutParams(shapeMarkerView.layoutParams.width - holderSize, shapeMarkerView.layoutParams.height - holderSize))
        roundView.x = (holderSize/2).toFloat()
        roundView.y = (holderSize/2).toFloat()
        setCanvas()
    }
    override fun markerClicked()
    {
        showMenu(shapeMarkerView)
    }
    override fun hideMenu()
    {

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
