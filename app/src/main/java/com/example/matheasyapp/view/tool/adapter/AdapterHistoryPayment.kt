package com.example.matheasyapp.view.tool.adapter

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.matheasyapp.R
import com.example.matheasyapp.itf.OnclickPaymentHistory
import com.example.matheasyapp.view.tool.model.LoanHistory

class AdapterHistoryPayment(
    private val items: ArrayList<LoanHistory>,
    private val context: Context
) : RecyclerView.Adapter<AdapterHistoryPayment.ItemViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterHistoryPayment.ItemViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_loan, parent, false)

        return ItemViewHolder(view)

    }

    override fun onBindViewHolder(holder: AdapterHistoryPayment.ItemViewHolder, position: Int) {

        val item = items[position]

        if (position == 0) {

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0, 0, 0, 0)
            holder.layoutParent.layoutParams = layoutParams
            holder.img_sysma.visibility = View.GONE
            holder.tv_month.text = "N"
            holder.tv_month.setTextColor(ContextCompat.getColor(context, R.color.title_unit_check))
            holder.tv_principal.text = item.interest
            holder.tv_principal.setTextColor(ContextCompat.getColor(context, R.color.title_unit_check))

            holder.tv_interest.text = item.borrow
            holder.tv_interest.setTextColor(ContextCompat.getColor(context, R.color.title_unit_check))

            holder.tv_payment.text = item.borrowTime
            holder.tv_payment.setTextColor(ContextCompat.getColor(context, R.color.title_unit_check))

            holder.layoutParent.setBackgroundResource(R.drawable.bg_title_history_loan)

        }

        if (position != 0) {
            holder.layoutParent.setBackgroundResource(R.drawable.item_history_uncheck)

            holder.img_sysma.visibility = View.VISIBLE
            holder.tv_month.visibility = View.GONE
            holder.tv_principal.setText(item.borrowPrincipal)
            holder.tv_interest.setText(item.interest)
            holder.tv_payment.setText(item.paymentSum)
        }

    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tv_month: TextView = itemView.findViewById(R.id.tv_month)
        var tv_principal: TextView = itemView.findViewById(R.id.tv_principal)
        var tv_interest: TextView = itemView.findViewById(R.id.tv_interest)
        var tv_payment: TextView = itemView.findViewById(R.id.tv_payment)
        var img_sysma: ImageView = itemView.findViewById(R.id.imgSysma)
        var layoutParent: LinearLayout = itemView.findViewById(R.id.itemLayout)

    }


    override fun getItemCount() = items.size


}