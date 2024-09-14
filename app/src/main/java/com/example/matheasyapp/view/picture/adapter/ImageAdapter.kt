package com.example.matheasyapp.view.picture.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide

import com.example.matheasyapp.R
import com.example.matheasyapp.view.home.itf.OnItemClickListenerPicture

class ImageAdapter(private val context: Context, private val imagePaths: List<String>, private  val clickItem : OnItemClickListenerPicture)  : BaseAdapter()  {
    override fun getCount(): Int {
        return imagePaths.size
    }

    override fun getItem(p0: Int): Any {
        return imagePaths[p0]
    }

    override fun getItemId(p0: Int): Long {
        return  p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.layout_item_picture, parent, false)
        val imageView: ImageView = view.findViewById(R.id.item_image)
        val cardItem : CardView = view.findViewById(R.id.cardItem)

        val imagePath = getItem(position) as String

        Glide.with(context)
            .load(imagePath)
            .error(android.R.drawable.ic_menu_close_clear_cancel)
            .into(imageView)

        cardItem.setOnClickListener {
            clickItem.onItemClick(imagePath)
        }



        return view
    }


}