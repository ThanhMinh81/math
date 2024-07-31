package com.example.matheasyapp.view.tool.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.matheasyapp.R
import com.example.matheasyapp.itf.OnclickPaymentHistory
import com.example.matheasyapp.view.tool.model.LoanHistory

class AdapterHistoryPayment(
    private val items: ArrayList<LoanHistory>,
    private val clickListener: OnclickPaymentHistory
) : RecyclerView.Adapter<AdapterHistoryPayment.ItemViewHolder>() {

        var listItemChecked: ArrayList<LoanHistory> = ArrayList<LoanHistory>()
    var showCheckBox: Boolean = false


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterHistoryPayment.ItemViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_history_payment, parent, false)

        return ItemViewHolder(view)


    }

    override fun onBindViewHolder(holder: AdapterHistoryPayment.ItemViewHolder, position: Int) {

        val item = items[position]


        // an checkbox trang thai ban dau
//        holder.cbSelected.visibility = View.VISIBLE
        holder.itemLayout.setBackgroundResource(R.drawable.item_history_uncheck)
        holder.line.visibility = View.GONE

        if (position == 0) {

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0, 0, 0, 0)
            holder.itemLayout.setPadding(0, 0, 0, 0)
            holder.itemLayout.layoutParams = layoutParams
            holder.itemLayout.setBackgroundResource(R.drawable.bg_null)

            holder.tv_month.text = "N"
            holder.tv_principal.text = item.interest
            holder.tv_interest.text = item.borrow
            holder.tv_payment.text = item.borrowTime

            holder.line.visibility = View.VISIBLE
            holder.cbSelected.visibility = View.GONE

        }

        if (position != 0) {
            holder.tv_month.text = "sygma"
            holder.tv_principal.setText(item.borrowPrincipal)
            holder.tv_interest.setText(item.interest)
            holder.tv_payment.setText(item.paymentSum)
            holder.cbSelected.visibility = View.GONE
        }


             holder.cbSelected.setOnClickListener {
                 if (holder.cbSelected.isChecked) {
                     clickListener.onClickPaymentHistory(item, true, listItemChecked.size)
                 } else {
                     clickListener.onClickPaymentHistory(item, false, listItemChecked.size)
                 }
             }


        if (showCheckBox ) {

                 if(position != 0){
                     holder.cbSelected.visibility = View.VISIBLE

                     if (listItemChecked.contains(item)) {

                         holder.cbSelected.isChecked = true
                         holder.itemLayout.setBackgroundResource(R.drawable.item_history_payment_check)

                     } else {

                         holder.itemLayout.setBackgroundResource(R.drawable.item_history_uncheck)
                         holder.cbSelected.isChecked = false
                     }
                 }


        } else {
            holder.cbSelected.visibility = View.GONE
        }

    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tv_month: TextView = itemView.findViewById(R.id.tv_month)
        var tv_principal: TextView = itemView.findViewById(R.id.tv_principal)
        var tv_interest: TextView = itemView.findViewById(R.id.tv_interest)
        var tv_payment: TextView = itemView.findViewById(R.id.tv_payment)
        var cbSelected: CheckBox = itemView.findViewById(R.id.cbSelected)
        var itemLayout: LinearLayout = itemView.findViewById(R.id.itemLayout)
        var line: View = itemView.findViewById(R.id.line)

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

    fun addCheckItem(item: LoanHistory) {
        listItemChecked.add(item)
    }

    fun removeCheckItem(item: LoanHistory?) {

        listItemChecked.remove(item)
    }

    fun getListChecked(): ArrayList<LoanHistory> {
        return listItemChecked
    }


}