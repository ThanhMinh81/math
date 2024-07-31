package com.example.matheasyapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.matheasyapp.R

import com.example.matheasyapp.model.Convert

import com.example.matheasyapp.itf.HistoryItemClickListener
import com.example.matheasyapp.itf.OnClickItemConver
import com.example.matheasyapp.model.History

class AdapterMoneyConverter(
    private val items: ArrayList<Convert>,
    private val clickItem: OnClickItemConver,
    private val context: Context
) : RecyclerView.Adapter<AdapterMoneyConverter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_conver_money, parent, false)

        return ItemViewHolder(view)

    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvName: TextView = itemView.findViewById(R.id.nameCountry)
        val tvSymboy: TextView = itemView.findViewById(R.id.signCountry)
        val ic_check: ImageView = itemView.findViewById(R.id.ic_check)
        val layoutItem: LinearLayout = itemView.findViewById(R.id.layout_item)

    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]

        holder.tvName.setText(item.name)
        holder.tvSymboy.setText(item.symbol)

        holder.layoutItem.setOnClickListener {
            clickItem.onClick(item)
        }

        if (item.check) {
            holder.ic_check.visibility = View.VISIBLE
            holder.tvName.setTextColor(context.resources.getColor(R.color.check))
            holder.tvSymboy.setTextColor(context.resources.getColor(R.color.check))
        } else {
            holder.tvName.setTextColor(context.resources.getColor(R.color.black))
            holder.tvSymboy.setTextColor(context.resources.getColor(R.color.black))
            holder.ic_check.visibility = View.GONE
        }

    }

    override fun getItemCount() = items.size

    fun setCheckItem(name: String) {
        val index = items.indexOfFirst { it.symbol == name }
        if (index != -1) {
            items.get(index).check = true
        }
    }


}
