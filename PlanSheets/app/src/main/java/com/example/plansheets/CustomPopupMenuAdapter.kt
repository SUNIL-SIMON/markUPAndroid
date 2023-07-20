package com.example.plansheets


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import com.example.plansheets.Sheet.MarkerMenuOptionsType
import com.example.plansheets.Sheet.MarkerMenuType
import com.example.plansheets.Sheet.SheetBaseViewInterface


class CustomPopupMenuAdapter(private val sheetBaseViewInterface : SheetBaseViewInterface, private val context: Context, private var list: ArrayList<MarkerMenuOptionsType>
    ) : BaseAdapter() {

    fun reinitialize(list: ArrayList<MarkerMenuOptionsType>)
    {
        this.list = list

        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(R.layout.item_popupmenu, parent, false)
        var btn: Button = convertView.findViewById(R.id.menu_button)
        btn.text = list.get(position).string
        btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if(list.get(position).option == MarkerMenuType.DELETE) {
                    sheetBaseViewInterface.deleteMarker()
                }
                if(list.get(position).option == MarkerMenuType.COPY) {
                    sheetBaseViewInterface.toastMarker()
                }
            }
        })

        return convertView
    }
}