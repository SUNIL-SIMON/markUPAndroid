package com.example.plansheets.ShapeMarkers

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.ListPopupWindow
import com.example.plansheets.*
import com.example.plansheets.Sheet.MarkerMenuOptionsType
import com.example.plansheets.Sheet.MarkerMenuType
import com.example.plansheets.Sheet.SheetBaseViewInterface


class RectMarkerView(private val sheetBaseViewInterface : SheetBaseViewInterface, context : Context) : ShapeMarkerView(sheetBaseViewInterface,context){
    val rectView = FrameLayout(context)
    var  popupMenu : ListPopupWindow = ListPopupWindow(context)
    override fun getShapeMarker(context: Context): View?
    {

        shapeMarkerView.addView(rectView)
        rectView.setLayoutParams(FrameLayout.LayoutParams(shapeDefaultSize - holderSize, shapeDefaultSize - holderSize))
        rectView.x = (holderSize/2).toFloat()
        rectView.y = (holderSize/2).toFloat()
        val rectViewShapedrawable = ShapeDrawable()
        rectViewShapedrawable.shape = RectShape()
        rectViewShapedrawable.paint.color = Color.GREEN
        rectViewShapedrawable.paint.strokeWidth = 10f
        rectViewShapedrawable.paint.style = Paint.Style.STROKE
        rectView.setBackground(rectViewShapedrawable)

        super.getShapeMarker(context)

        addShapeHolders(context)

        return shapeMarkerView
    }
    override fun holderMoved()
    {
        reconfigureRect()
        super.holderMoved()
    }
    override fun reconfigureRect()
    {
        rectView.setLayoutParams(FrameLayout.LayoutParams(shapeMarkerView.layoutParams.width - holderSize, shapeMarkerView.layoutParams.height - holderSize))
        rectView.x = (holderSize/2).toFloat()
        rectView.y = (holderSize/2).toFloat()
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
        popupMenu!!.width = ViewGroup.LayoutParams.WRAP_CONTENT
        val popUpMenuDrawable = ShapeDrawable()
        popUpMenuDrawable.shape = RectShape()
        popUpMenuDrawable.paint.color = Color.GREEN
        popupMenu!!.setBackgroundDrawable(popUpMenuDrawable)
        popupMenu!!.setAdapter(adapter)
        popupMenu.show()
    }

}
