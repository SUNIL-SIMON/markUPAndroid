package com.example.plansheets

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.plansheets.Sheet.MarkedGroupCellType
import com.example.plansheets.Sheet.MarkedInfoType
import com.example.plansheets.Sheet.PLMShapeType

class SideLayerListAdapter(
    private val context: Context,
) : BaseExpandableListAdapter() {

    var dataList: MutableList<MarkedGroupCellType>? = null

    fun reinitialize(dataList: MutableList<MarkedGroupCellType>)
    {
        this.dataList = dataList

        notifyDataSetChanged()
    }

    override fun getGroupCount(): Int {
        return dataList?.size ?: 0
    }

    override fun getChildrenCount(p0: Int): Int {
        if(dataList != null)
        {
            return dataList!![p0].markers.size
        }
        return 0
    }

    override fun getGroup(p0: Int): Any {
        return dataList!![p0]
    }

    override fun getChild(p0: Int, p1: Int): Any {
        return dataList!![p0].markers[p1]
    }

    override fun getGroupId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getChildId(p0: Int, p1: Int): Long {
        return p1.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }
    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sidelayerlistitem, null)
        }
        val listTitleTextView = convertView?.findViewById<TextView>(R.id.SideLayerListItem_textview)
        listTitleTextView?.setTypeface(null, Typeface.BOLD)
        listTitleTextView?.text = dataList!![listPosition].title
        return convertView!!
    }
    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sidelayerlistitem, parent, false)
        }
        val listTitleTextView = convertView?.findViewById<TextView>(R.id.SideLayerListItem_textview)
        listTitleTextView?.setTypeface(null, Typeface.BOLD)
        listTitleTextView?.text = getMarkerTitle(dataList!![listPosition].markers[expandedListPosition].markedInfos)//dataList[listPosition].markers[expandedListPosition].text
        return convertView!!
    }
    fun getMarkerTitle(markedInfos : MarkedInfoType):String
    {
        if(markedInfos.type == PLMShapeType.RECT){
            return "Rect"
        }
        if(markedInfos.type == PLMShapeType.ARCS){
            return "Cloud"
        }
        if(markedInfos.type == PLMShapeType.TRIANGLE){
            return "Triangle"
        }
        if(markedInfos.type == PLMShapeType.ROUND){
            return "Ellipse"
        }
        if(markedInfos.type == PLMShapeType.LINE){
            return "Line"
        }
        if(markedInfos.type == PLMShapeType.PATH){
            return "Pen"
        }
        return ""
    }
}
