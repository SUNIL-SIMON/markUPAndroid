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

class CustomExpandableListAdapter internal constructor(
    private val context: Context,
    private val dataList: MutableList<MarkedGroupCellType>//HashMap<String, List<String>>
) : BaseExpandableListAdapter() {
    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return getMarkerTitle(this.dataList[listPosition].markers[expandedListPosition].markedInfos)
    }
    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }
    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val expandedListText = getChild(listPosition, expandedListPosition) as String
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item, null)
        }
        val expandedListTextView = convertView!!.findViewById<TextView>(R.id.listView)
        expandedListTextView.text = expandedListText
        return convertView
    }
    override fun getChildrenCount(listPosition: Int): Int {
        return this.dataList[listPosition].markers.size
    }
    override fun getGroup(listPosition: Int): Any {
        return this.dataList[listPosition].title
    }
    override fun getGroupCount(): Int {
        return this.dataList.size
    }
    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }
    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val listTitle = getGroup(listPosition) as String
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item, null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.listView)
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle
        return convertView
    }
    override fun hasStableIds(): Boolean {
        return false
    }
    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
    fun getMarkerTitle(markedInfos : MarkedInfoType):String
    {
        if(markedInfos.type == PLMShapeType.RECT){
            return "Rect"
        }
        if(markedInfos.type == PLMShapeType.ARCS){
            return "Cloud"
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
