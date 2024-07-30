package com.example.matheasyapp

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment


class DrawFragment : Fragment() {

    private lateinit var paint: DrawView
    private lateinit var btnPen : ImageView
    private lateinit var btnErase : ImageView
    private lateinit var btnUndo : ImageView
    private lateinit var btnRedo : ImageView
    private lateinit var btnNext : ImageView

    private lateinit var  btnTrash : ImageView
    private lateinit var  layoutErase : LinearLayout
    private lateinit var  layoutPen :  LinearLayout


    private lateinit var iView : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

            iView = inflater.inflate(R.layout.fragment_draw, container, false)
            btnPen = iView.findViewById(R.id.btnPen)
            btnErase = iView.findViewById(R.id.btnErase)
            btnUndo = iView.findViewById(R.id.btnUndo)
            btnRedo = iView.findViewById(R.id.btnRedo)
            btnNext = iView.findViewById(R.id.btnNext)
            paint = this.iView.findViewById(R.id.draw_view)
            btnTrash = iView.findViewById(R.id.btnTrash)

            btnUndo.setOnClickListener { paint.undo() }
            btnRedo.setOnClickListener { paint.redo() }
            layoutErase = iView.findViewById(R.id.layoutErase)
            layoutPen = iView.findViewById(R.id.layoutPen)

        // enable paint
           layoutPen.translationY = -50F
            paint.changeStatePaint(true)


        layoutErase.setOnClickListener {
            layoutErase.translationY = -50F
            layoutPen.translationY = 0F
            paint.changeStatePaint(false)
        }

        layoutPen.setOnClickListener {
            layoutPen.translationY = -50F
            layoutErase.translationY = 0F
            paint.changeStatePaint(true)
        }

        btnTrash.setOnClickListener {
            paint.clear()
        }

        val vto = paint.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                paint.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = paint.measuredWidth
                val height = paint.measuredHeight
                paint.init(height, width)
            }
        })

        return iView
    }

}