package com.example.plansheets.Markers


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import com.example.plansheets.R
import com.example.plansheets.Sheet.MarkerMenuOptionsType
import com.example.plansheets.Sheet.MarkerMenuType
import com.example.plansheets.Sheet.SheetBaseViewInterface


class ShapeMarkerViewMenuOptionsAdapter(private val sheetBaseViewInterface : SheetBaseViewInterface, private val context: Context, private var list: ArrayList<MarkerMenuOptionsType>
    ) : BaseAdapter() {

    fun reinitialize(list: ArrayList<MarkerMenuOptionsType>)
    {
        this.list = list

        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(p0: Int): Any {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.shapemarkerviewmenus, parent, false)
        }
        var btn: Button? = convertView?.findViewById(R.id.shapemarkerviewmenubutton)
        btn?.text = list[position].menuTitle
        btn?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if(list[position].option == MarkerMenuType.DELETE) {
                    sheetBaseViewInterface.deleteMarker()
                }
                if(list[position].option == MarkerMenuType.COPY) {
                    sheetBaseViewInterface.toastMarker()
                }
            }
        })

        return convertView!!
    }


}