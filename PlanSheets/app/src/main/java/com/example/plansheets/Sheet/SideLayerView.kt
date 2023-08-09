package com.example.plansheets.Sheet

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.*
import com.example.plansheets.Markers.ShapeMarkerView
import com.example.plansheets.SideLayerListAdapter
import java.util.ArrayList

class SideLayerView(private val sheetBaseViewInterface : SheetBaseViewInterface, context : Context) : ShapeMarkerView(sheetBaseViewInterface,context) {
    val sideLayerView = FrameLayout(context)
    val expandButton = Button(context)
    val contractButton = Button(context)
    var layerListView = ExpandableListView(context)

    private var adapter: SideLayerListAdapter = SideLayerListAdapter(context)

    var markerGroups : MutableList<MarkedGroupCellType> =  ArrayList()

    fun getSideLayerView(context: Context): View? {

        sideLayerView.setBackgroundColor(Color.LTGRAY)

        expandButton.text = "+"
        sideLayerView.addView(expandButton)
        expandButton.setLayoutParams(FrameLayout.LayoutParams(60, 60))
        expandButton.x = 0f
        expandButton.y = 0f
        expandButton.setOnClickListener()
        {
            sheetBaseViewInterface.toggleSideLayerExpansion()
        }
        sideLayerView.addView(layerListView)
        layerListView.setBackgroundColor(Color.LTGRAY)
        layerListView.setLayoutParams(FrameLayout.LayoutParams(0, 0))
        layerListView.x = 0f
        layerListView.y = 60f

        contractButton.text = "-"
        sideLayerView.addView(contractButton)
        contractButton.setLayoutParams(FrameLayout.LayoutParams(60, 60))
        contractButton.x = 0f
        contractButton.y = 0f
        contractButton.setOnClickListener()
        {
            sheetBaseViewInterface.toggleSideLayerExpansion()
        }
        setUplayerListView()
        reloadTableView()
        return sideLayerView
    }
    open fun performOrientation(screenWidth:Int,screenHeight : Int)
    {
        contractButton.x = (sideLayerView.layoutParams.width - 60).toFloat()
    }
    fun setExpandedContractState()
    {
        if(sheetBaseViewInterface.isSideLayerExpanded())
        {
            expandButton.visibility = View.GONE
            contractButton.visibility = View.VISIBLE
            layerListView.visibility = View.VISIBLE
        }
        else{
            expandButton.visibility = View.VISIBLE
            contractButton.visibility = View.GONE
            layerListView.visibility = View.GONE
        }
    }
    fun setUplayerListView()
    {
        layerListView.setAdapter(adapter)
        layerListView.setOnGroupExpandListener { groupPosition ->
//            Toast.makeText(
//                context,
//                markerGroups[groupPosition].title + " List Expanded.",
//                Toast.LENGTH_SHORT
//            ).show()
        }
        layerListView.setOnGroupCollapseListener { groupPosition ->
//            Toast.makeText(
//                context,
//                markerGroups[groupPosition].title + " List Collapsed.",
//                Toast.LENGTH_SHORT
//            ).show()
        }
        layerListView.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
            false
        }
    }
    fun reloadTableView() {
        adapter.reinitialize(markerGroups)
    }

}
