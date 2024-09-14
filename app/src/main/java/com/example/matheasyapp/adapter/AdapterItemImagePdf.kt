package com.example.matheasyapp.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.matheasyapp.R
import com.lyrebirdstudio.croppylib.cropview.CropView

class AdapterItemImagePdf(
    private val context: Context,
    private val bitmapMap: MutableMap<Int, Bitmap?>,  // sử dụng MutableMap
    // callback để yêu cầu load trang nếu cần
) : RecyclerView.Adapter<AdapterItemImagePdf.ImageViewHolder>() {

    var currentItemShow: Int = 0

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cropView: CropView = itemView.findViewById(R.id.cropView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_image_pdf, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val bitmap = bitmapMap[position]

        if (bitmap != null) {
            // Nếu bitmap đã được load, hiển thị nó
            holder.cropView.post {
                holder.cropView.setBitmap(bitmap)
            }
        } else {
            // Nếu bitmap chưa được load, gọi callback để load
//            holder.cropView.setBitmap(null) // Hiển thị trang trắng hoặc spinner nếu cần
            // yêu cầu load trang PDF
        }

        currentItemShow = position
    }

    override fun getItemCount(): Int {
        return bitmapMap.size
    }
}