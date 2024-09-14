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
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.matheasyapp.R

import com.example.matheasyapp.model.Convert

import com.example.matheasyapp.itf.HistoryItemClickListener
import com.example.matheasyapp.itf.OnClickItemConver
import com.example.matheasyapp.model.Currency
import com.example.matheasyapp.model.History

class AdapterMoneyConverter(
    private val items: ArrayList<Currency>,
    private val clickItem: OnClickItemConver,
    private val context: Context
) : RecyclerView.Adapter<AdapterMoneyConverter.ItemViewHolder>(), Filterable {

    private var displayList: MutableList<Currency> = items.toMutableList()
    private var originalList: ArrayList<Currency> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

//        originalList = items.toList()
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_conver_money, parent, false)

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
            clickItem.onClickCurrency(item)
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

    override fun getFilter(): Filter {

//        Log.d("534523525252",originalList.size.toString())

        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase()?.trim() ?: ""


                val filteredList = if (query.isEmpty()) {
                    originalList
                } else {
                    originalList.filter {
                        it.name.lowercase().contains(query) || it.symbol.lowercase().contains(query)
                    }
                }

                // Prepare the filter result
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                // Ensure the results are not null and are of the correct type
                if (results?.values is List<*>) {
                    @Suppress("UNCHECKED_CAST")
                    val filteredList = results.values as List<Currency>

                    // Clear the current items and add the filtered list
                    items.clear()
                    items.addAll(filteredList)

                    // Notify the adapter to refresh the UI
                    notifyDataSetChanged()
                }
            }
        }
    }


    override fun getItemCount() = items.size


    fun updateFullList() {
        items.clear()
        items.addAll(originalList)

        notifyDataSetChanged()
    }

    fun setListSearch(list : List<Currency>){
        originalList.clear()
        originalList.addAll(list)
    }


}
