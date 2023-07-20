package com.example.plansheets.Sheet

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.*
import com.example.plansheets.CustomExpandableListAdapter
import com.example.plansheets.ShapeMarkerView
import java.util.ArrayList

class SideLayerView(private val sheetBaseViewInterface : SheetBaseViewInterface, context : Context) : ShapeMarkerView(sheetBaseViewInterface,context) {
    val sideLayerView = FrameLayout(context)
    val expandButton = Button(context)
    val contractButton = Button(context)
    var layerListView = ExpandableListView(context)

    private var adapter: ExpandableListAdapter? = null

    var markerGroups : MutableList<MarkedGroupCellType> =  ArrayList()

    fun getSideLayerView(context: Context): View? {

        sideLayerView.setBackgroundColor(Color.WHITE)

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
        layerListView.setBackgroundColor(Color.WHITE)
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
    fun reloadTableView()
    {
            adapter = CustomExpandableListAdapter(context, markerGroups)
            layerListView.setAdapter(adapter)
            layerListView.setOnGroupExpandListener { groupPosition ->
                Toast.makeText(
                    context,
                    markerGroups[groupPosition].title + " List Expanded.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            layerListView.setOnGroupCollapseListener { groupPosition ->
                Toast.makeText(
                    context,
                    markerGroups[groupPosition].title + " List Collapsed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            layerListView.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
//                Toast.makeText(
//                    context,
//                    "Clicked: " + markerGroups[groupPosition].title + " -> " + listData[(
//                            titleList as
//                                    java.util.ArrayList<String>
//                            )
//                            [groupPosition]]!!.get(
//                        childPosition
//                    ),
//                    Toast.LENGTH_SHORT
//                ).show()
                false
            }
    }
}
