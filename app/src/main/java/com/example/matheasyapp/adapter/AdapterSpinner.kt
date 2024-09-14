package com.example.matheasyapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.matheasyapp.R

class AdapterSpinner(private val context: Context, private val fruitList: List<String>? , private  val textBold : Boolean) :
    BaseAdapter() {
    override fun getCount(): Int {
        return fruitList?.size ?: 0
    }

    override fun getItem(i: Int): Any {
        return fruitList?.get(i) ?: Any()
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, convertView: View?, viewGroup: ViewGroup): View {

        if(textBold){
            // Tái sử dụng view nếu có, nếu không thì inflate một view mới
            val rootView = convertView ?: LayoutInflater.from(context)
                .inflate(R.layout.item_spinner, viewGroup, false)

            val txtName = rootView.findViewById<TextView>(R.id.spName)

            txtName.text = fruitList!![i]

            return rootView
        }else{
            // Tái sử dụng view nếu có, nếu không thì inflate một view mới
            val rootView = convertView ?: LayoutInflater.from(context)
                .inflate(R.layout.item_spinner_normal, viewGroup, false)

            val txtName = rootView.findViewById<TextView>(R.id.spName)

            txtName.text = fruitList!![i]

            return rootView
        }

    }
}
