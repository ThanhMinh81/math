package com.example.matheasyapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.matheasyapp.R
import com.example.matheasyapp.model.PdfFile
import com.example.matheasyapp.view.home.pdf.onClickItemPDF
import io.reactivex.annotations.NonNull

class PDFAdapter(
    var context: Context,
    var pdffiles: ArrayList<PdfFile>,
    var itemClick: onClickItemPDF,
    var listStart: ArrayList<PdfFile>,
    var allPDF: Boolean = true
) : RecyclerView.Adapter<PDFAdapter.AdapterViewholder>() {


    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): AdapterViewholder {
        val inflater = LayoutInflater.from(context)
        val v: View = inflater.inflate(R.layout.item_pdf, parent, false)
        return AdapterViewholder(v, context, itemClick)
    }

    override fun onBindViewHolder(
        @NonNull holder: AdapterViewholder,
        @SuppressLint("RecyclerView") position: Int
    ) {

        if (allPDF) {
            val pdfFile = pdffiles[position]
            holder.filename.text = pdfFile.name
            holder.layoutContentPDF.setOnClickListener {
                itemClick.onClickItem(pdfFile)
            }
            holder.tvSize.text = pdfFile.size
            holder.tvTime.text = pdfFile.timeEdit

            val pdfStart = listStart.firstOrNull { it.id.equals(pdfFile.id) }

            if (pdfStart != null) {
                holder.imgStart.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_start_check)
                )
            } else {
                holder.imgStart.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_start_uncheck)
                )
            }

            val currentDrawable: Drawable = holder.imgStart.drawable
            val drawableToCheck = ContextCompat.getDrawable(context, R.drawable.ic_start_check)
            val bitmap1 = drawableToBitmap(currentDrawable)
            val bitmap2 = drawableToBitmap(drawableToCheck!!)

            if (bitmap1.sameAs(bitmap2)) {
                holder.imgStart.setOnClickListener {
                    itemClick.onClickItemStart(pdfStart!!, false)
                }
            } else {
                holder.imgStart.setOnClickListener {
                    itemClick.onClickItemStart(pdfFile, true)
                }
            }
            holder.icMenu.setOnClickListener {
                itemClick.onClickItemEditPDF(pdfFile)
            }

        } else {
            val pdfFile = listStart[position]
            holder.filename.text = pdfFile.name
            holder.layoutContentPDF.setOnClickListener {
                itemClick.onClickItem(pdfFile)
            }
            holder.tvSize.text = pdfFile.size
            holder.tvTime.text = pdfFile.timeEdit

            holder.imgStart.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_start_check
                )
            )

            holder.imgStart.setOnClickListener {
                itemClick.onClickItemStart(pdfFile, false)

            }
            holder.icMenu.setOnClickListener {
                itemClick.onClickItemEditPDF(pdfFile)
            }
        }
    }

    override fun getItemCount(): Int {
        if (allPDF) {
            return pdffiles.size
        } else {
            return listStart.size
        }
    }

    class AdapterViewholder(@NonNull itemView: View, context: Context, itemClick: onClickItemPDF) :
        RecyclerView.ViewHolder(itemView) {
        var filename: TextView = itemView.findViewById(R.id.fileName)
        var layoutContentPDF: LinearLayout = itemView.findViewById(R.id.layoutContentPDF)
        var tvSize: TextView = itemView.findViewById(R.id.tvSizeFile)
        var tvTime: TextView = itemView.findViewById(R.id.file_time)
        var imgStart: ImageView = itemView.findViewById(R.id.ic_start)

        //        var menuPDF : LinearLayout = itemView.findViewById(R.id.layout_menu_pdf)
        var icMenu: ImageView = itemView.findViewById(R.id.ic_menu)
    }

    fun setListPDFStart(list: List<PdfFile>) {
        this.listStart.clear()
        this.listStart.addAll(list)

    }

    fun setChangeDisplay(value: Boolean, listNew: List<PdfFile>) {
        if (value) {
            this.allPDF = value
//            this.pdffiles.clear()
//            this.pdffiles.addAll(listNew)
            this.notifyDataSetChanged()
        } else {
            this.allPDF = value
        }
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap: Bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap

    }


    fun updateListPDF(value: List<PdfFile>) {

        this.pdffiles.clear()
        this.pdffiles.addAll(value)

    }

    fun sortPDF(list: ArrayList<PdfFile>) {

        val startPaths = list.map { it.path }.toSet()
        pdffiles.forEach { pdfFile ->
            if (startPaths.contains(pdfFile.path)) {
                pdfFile.piority = true
            }
        }
        pdffiles.sortedWith(compareByDescending { it.piority })
        this.notifyDataSetChanged()
    }




}
