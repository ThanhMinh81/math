package com.example.matheasyapp.view.history.historyscan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.matheasyapp.R
import com.example.matheasyapp.db.CameraHistory
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log

class AdapterHistoryCamera(
    private val items: ArrayList<CameraHistory>,
    private val clickListener: EventOnClickHistory
) : RecyclerView.Adapter<AdapterHistoryCamera.ItemViewHolder>() {

    var listItemChecked: ArrayList<CameraHistory> = ArrayList<CameraHistory>()
    var showCheckBox: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_layout_history_camera, parent, false)

        return ItemViewHolder(view)

    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cbSelected: CheckBox = itemView.findViewById(R.id.cbSelected)

        val image : ImageView = itemView.findViewById(R.id.img_history)
        val itemLayout: LinearLayout = itemView.findViewById(R.id.layoutItem)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]

        Log.d("523523523", "352345")

        holder.image.setPadding(0, 0, 0, 0)
        // an checkbox trang thai ban dau
        holder.cbSelected.visibility = View.VISIBLE
        holder.itemLayout.setBackgroundResource(R.drawable.item_history_uncheck)

        val bitmap = base64ToBitmap(item.base64)
        if(bitmap != null){
            holder.image.setImageBitmap(bitmap)
        }

        holder.cbSelected.setOnClickListener {
            if (holder.cbSelected.isChecked) {
                clickListener.onClickItem(item, true, listItemChecked.size)
            } else {
                clickListener.onClickItem(item, false, listItemChecked.size)
            }
        }

        if (showCheckBox) {
            holder.cbSelected.visibility = View.VISIBLE

//            val paddingInDp = 16
//            val scale = holder.itemView.context.resources.displayMetrics.density
//            val paddingInPx = (paddingInDp * scale + 0.5f).toInt()

//            holder.image.setPadding(paddingInPx, paddingInPx, 0, paddingInPx)

            if (listItemChecked.contains(item)) {
                holder.cbSelected.isChecked = true
                holder.itemLayout.setBackgroundResource(R.drawable.item_history_check)

            } else {

                holder.itemLayout.setBackgroundResource(R.drawable.item_history_uncheck)
                holder.cbSelected.isChecked = false
            }

        } else {
            holder.image.setPadding(0, 0, 0, 0)
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

    fun addCheckItem(item: CameraHistory) {
        listItemChecked.add(item)
    }

    fun removeCheckItem(item: CameraHistory?) {

        listItemChecked.remove(item)
    }

    fun getListChecked() : ArrayList<CameraHistory> {
        return listItemChecked
    }

    fun base64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedString = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        }
    }




}