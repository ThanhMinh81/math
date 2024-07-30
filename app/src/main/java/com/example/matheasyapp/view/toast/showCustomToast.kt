package com.example.matheasyapp.view.toast
import android.content.Context
import android.view.LayoutInflater
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.matheasyapp.R

fun showCustomToast(context: Context, message: String) {
    val inflater = LayoutInflater.from(context)
    val layout: View = inflater.inflate(R.layout.layout_toast, null)

    val toastMessage = layout.findViewById<TextView>(R.id.toast_message)

    toastMessage.text = message

    val toast = Toast(context)
    toast.duration = Toast.LENGTH_SHORT
    toast.view = layout
    toast.setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, 0)
    toast.show()
}