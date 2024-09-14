package com.example.matheasyapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.graphics.green
import androidx.recyclerview.widget.RecyclerView
import com.example.matheasyapp.R
import com.example.matheasyapp.itf.HistoryItemClickListener
import com.example.matheasyapp.model.History

class AdapterHistory(
    private val items: ArrayList<History>,
    private val clickListener: HistoryItemClickListener
) : RecyclerView.Adapter<AdapterHistory.ItemViewHolder>() {

    var listItemChecked: ArrayList<History> = ArrayList<History>()
    var showCheckBox: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_layout_history, parent, false)

        return ItemViewHolder(view)

    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val calculation: TextView = itemView.findViewById(R.id.tvCalculation)
        val result: TextView = itemView.findViewById(R.id.tvResult)
        val cbSelected: CheckBox = itemView.findViewById(R.id.cbSelected)
        val itemLayout: LinearLayout = itemView.findViewById(R.id.layoutItem)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]

        // an checkbox trang thai ban dau
        holder.cbSelected.visibility = View.VISIBLE
        holder.itemLayout.setBackgroundResource(R.drawable.item_history_uncheck)

        // all checkbox check
        holder.calculation.text = item.calculation
        holder.result.text = item.result

        holder.cbSelected.setOnClickListener {
            if (holder.cbSelected.isChecked) {
                clickListener.onItemClick(item, true, listItemChecked.size)
            } else {
                clickListener.onItemClick(item, false, listItemChecked.size)
            }
        }

        if (showCheckBox) {
            holder.cbSelected.visibility = View.VISIBLE

            if (listItemChecked.contains(item)) {

                holder.cbSelected.isChecked = true
                holder.itemLayout.setBackgroundResource(R.drawable.item_history_check)

            } else {

                holder.itemLayout.setBackgroundResource(R.drawable.item_history_uncheck)
                holder.cbSelected.isChecked = false
            }

        } else {
            holder.cbSelected.visibility = View.GONE
        }

    }

    override fun getItemCount() = items.size

    // checked all checkbox
    fun selectedAllItemChecked() {
        listItemChecked.addAll(items)
    }

    fun clearAllItemChecked() {
        listItemChecked.clear()
    }


    fun showCheckbox(value: Boolean) {
        showCheckBox = value
    }

    fun addCheckItem(item: History) {
        listItemChecked.add(item)
    }

    fun removeCheckItem(item: History?) {

        listItemChecked.remove(item)
    }

    fun getListChecked() : ArrayList<History> {
        return listItemChecked
    }




}