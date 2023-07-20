package com.example.plansheets.ShapeMarkers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.FrameLayout
import androidx.appcompat.widget.ListPopupWindow
import com.example.plansheets.*
import com.example.plansheets.Sheet.MarkerMenuOptionsType
import com.example.plansheets.Sheet.MarkerMenuType
import com.example.plansheets.Sheet.SheetBaseViewInterface
import java.util.*

class EllipseMarkerView(private val sheetBaseViewInterface : SheetBaseViewInterface, context : Context) : ShapeMarkerView(sheetBaseViewInterface,context){
    val roundView = ImageView(context)
    var  popupMenu : ListPopupWindow = ListPopupWindow(context)
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
        canvas.drawOval(0f,0f,roundView.layoutParams.width.toFloat(),roundView.layoutParams.height.toFloat(),paint)
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
