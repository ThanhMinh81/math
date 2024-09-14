package com.example.matheasyapp.view.home.pdf

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.matheasyapp.R
import com.example.matheasyapp.model.SearchPDF

interface OnItemClickListener {
    fun onItemClick(item: SearchPDF)
    fun onClearClick(item: SearchPDF)
}

class SearchAdapter(
    context: Context,
    private val items: ArrayList<SearchPDF>,
    private val listener: OnItemClickListener // Thêm listener vào adapter
) : ArrayAdapter<SearchPDF>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_search_pdf, parent, false)
        val textView: TextView = view.findViewById(R.id.textName)
        val iconClear: ImageView = view.findViewById(R.id.ic_clear)

        val item = items[position]
        textView.text = item.value
//        textView.setOnClickListener { listener.onItemClick(item) }
        iconClear.setOnClickListener { listener.onClearClick(item) }

        return view
    }
}