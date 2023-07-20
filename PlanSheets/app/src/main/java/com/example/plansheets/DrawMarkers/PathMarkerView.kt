package com.example.plansheets.DrawMarkers

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.ListPopupWindow
import com.example.plansheets.*
import com.example.plansheets.Sheet.MarkerMenuOptionsType
import com.example.plansheets.Sheet.MarkerMenuType
import com.example.plansheets.Sheet.SheetBaseViewInterface
import java.util.ArrayList

class PathMarkerView (private val sheetBaseViewInterface : SheetBaseViewInterface, context : Context) : ShapeMarkerView(sheetBaseViewInterface,context) {
    val roundView = ImageView(context)
    var  popupMenu : ListPopupWindow = ListPopupWindow(context)
    var prevPoint = PointF(0f,0f)

    override fun getShapeMarker(context: Context): View?
    {
        shapeMarkerView.addView(roundView)
        roundView.x = 0f
        roundView.y = 0f
        roundView.setLayoutParams(FrameLayout.LayoutParams(shapeMarkerView.layoutParams.width , shapeMarkerView.layoutParams.height ))

        super.getShapeMarker(context)

        addShapeHolders(context)
        return shapeMarkerView
    }
    fun setCanvasDrawable(points : MutableList<PointF>)
    {
        val bitmap = Bitmap.createBitmap(roundView.layoutParams.width, roundView.layoutParams.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = Color.GREEN
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4F
        if(points.size > 0) {
            prevPoint = points[0]
        }
        for(point in points){
            canvas.drawLine(prevPoint.x,prevPoint.y,point.x,point.y,paint)
            prevPoint = point
        }
        roundView.setImageBitmap(bitmap)
    }
    fun setCanvas()
    {
        val points = markedInfos.originalDetails.originalLayerPoints
        val bitmap = Bitmap.createBitmap(roundView.layoutParams.width, roundView.layoutParams.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = Color.GREEN
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4F
        if(points.size > 0) {
            val ptx = points[0].x * roundView.layoutParams.width
            val pty = points[0].y * roundView.layoutParams.height
            prevPoint = PointF(ptx,pty)
        }
        for(point in points){
            val ptx = point.x * roundView.layoutParams.width
            val pty = point.y * roundView.layoutParams.height
            canvas.drawLine(prevPoint.x,prevPoint.y,ptx,pty,paint)
            prevPoint = PointF(ptx,pty)
        }
        roundView.setImageBitmap(bitmap)
    }

    override fun holderMoved()
    {
        reconfigureRect()
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
        roundView.setLayoutParams(FrameLayout.LayoutParams(shapeMarkerView.layoutParams.width - holderSize, shapeMarkerView.layoutParams.height - holderSize))
        roundView.x = (holderSize/2).toFloat()
        roundView.y = (holderSize/2).toFloat()
        setCanvas()
    }
    private fun showMenu(view: View) {
        var list : ArrayList<MarkerMenuOptionsType> = ArrayList()
        list.add(MarkerMenuOptionsType(MarkerMenuType.DELETE,"Delete"))
        list.add(MarkerMenuOptionsType(MarkerMenuType.COPY,"Copy"))
        list.add(MarkerMenuOptionsType(MarkerMenuType.PASTE,"Paste"))
        list.add(MarkerMenuOptionsType(MarkerMenuType.INFO,"Info"))


        var adapter: CustomPopupMenuAdapter =  CustomPopupMenuAdapter(sheetBaseViewInterface,context, list)
        popupMenu!!.anchorView = view
        popupMenu!!.height = ViewGroup.LayoutParams.WRAP_CONTENT
        popupMenu!!.width = 100//ViewGroup.LayoutParams.WRAP_CONTENT
        val popUpMenuDrawable = ShapeDrawable()
        popUpMenuDrawable.shape = RectShape()
        popUpMenuDrawable.paint.color = Color.GREEN
        popupMenu!!.setBackgroundDrawable(popUpMenuDrawable)
        popupMenu!!.setAdapter(adapter)
        popupMenu.show()
    }
}