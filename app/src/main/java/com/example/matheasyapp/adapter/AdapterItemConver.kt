package com.example.matheasyapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.matheasyapp.R
import com.example.matheasyapp.model.Convert
import com.example.matheasyapp.itf.OnClickItemConver

class AdapterItemConver(
    private var items: ArrayList<Convert>,
    private val clickItem: OnClickItemConver,
    private val context: Context
) : RecyclerView.Adapter<AdapterItemConver.ItemViewHolder>(), Filterable {

    private var displayList: MutableList<Convert> = items.toMutableList()
    private val originalList: List<Convert> = items.toList() // Lưu danh sách ban đầu

    private var filteredCount : Int  = 0

    private var lastClickTime: Long = 0
    private val CLICK_INTERVAL: Long = 1000 // 2 giây



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_unitconvert, parent, false)
        return ItemViewHolder(view)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvNameUnit)
        val ic_check: ImageView = itemView.findViewById(R.id.ic_check)
        val layoutItem: LinearLayout = itemView.findViewById(R.id.itemLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = displayList[position]

        holder.tvName.text = item.name

//        holder.layoutItem.setOnClickListener {
//            clickItem.onClick(item)
//        }

        holder.layoutItem.setOnClickListener {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime >= CLICK_INTERVAL) {
                lastClickTime = currentTime
                clickItem.onClick(item)
            } else {
                // Không xử lý nhấp chuột nếu chưa đủ thời gian
            }
        }

        if (item.check) {
            holder.ic_check.visibility = View.VISIBLE
            holder.tvName.setTextColor(context.resources.getColor(R.color.title_unit_check))
            holder.layoutItem.setBackgroundResource(R.drawable.bg_item_convert_check)
        } else {
            holder.tvName.setTextColor(context.resources.getColor(R.color.title_unit_uncheck))
            holder.ic_check.visibility = View.GONE
            holder.layoutItem.setBackgroundResource(R.drawable.bg_item_convert_uncheck)
        }
    }

    override fun getItemCount() = displayList.size

    fun setCheckItem(name: String) {
        val index = displayList.indexOfFirst { it.symbol == name }
        if (index != -1) {
            displayList[index].check = true
            notifyItemChanged(index)
        }
    }

    // Triển khai Filter
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase() ?: ""

                val filteredList = if (query.isEmpty()) {
                    originalList // Sử dụng danh sách ban đầu nếu không có  tìm kiếm
                } else {
                    originalList.filter {
                        it.name.lowercase().contains(query)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                @Suppress("UNCHECKED_CAST") displayList.clear()
                displayList.addAll(results?.values as List<Convert>)
                filteredCount = displayList.size
                notifyDataSetChanged()
            }

        }
    }



    fun updateFullList() {
        displayList.clear()
        displayList.addAll(originalList)
        filteredCount = displayList.size
        notifyDataSetChanged()
    }


}
