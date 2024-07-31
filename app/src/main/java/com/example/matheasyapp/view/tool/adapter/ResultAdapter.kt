package com.example.matheasyapp.view.tool.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.matheasyapp.R
import com.example.matheasyapp.view.tool.model.LoanPayment

class ResultAdapter(private val context: Context, private val loanPayments: List<LoanPayment?>) :
    RecyclerView.Adapter<ResultAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_loan_payment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val loanPayment = loanPayments[position]
        holder.bind(loanPayment!!, position)
    }

    override fun getItemCount(): Int {
        return loanPayments.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMonth: TextView = itemView.findViewById(R.id.tv_month)
        private val tvPrincipal: TextView = itemView.findViewById(R.id.tv_principal)
        private val tvInterest: TextView = itemView.findViewById(R.id.tv_interest)
        private val tvPayment: TextView = itemView.findViewById(R.id.tv_payment)
        private val tvBalance: TextView = itemView.findViewById(R.id.tv_balance)
        private val viewLineTop: View = itemView.findViewById(R.id.lineTop)
        private val viewLineBottom: View = itemView.findViewById(R.id.lineBottom)


        fun bind(loanPayment: LoanPayment, position: Int) {


            if (position == 0) {
                // first element
                viewLineTop.visibility = View.GONE
                viewLineBottom.visibility = View.VISIBLE

                tvPrincipal.setTextColor(context.resources.getColor(R.color.header_item, null))
                tvInterest.setTextColor(context.resources.getColor(R.color.header_item, null))
                tvPayment.setTextColor(context.resources.getColor(R.color.header_item, null))
                tvBalance.setTextColor(context.resources.getColor(R.color.header_item, null))

            }
//
            if (position == loanPayments.size - 1) {
                viewLineTop.visibility = View.VISIBLE
                viewLineBottom.visibility = View.GONE

                tvPrincipal.setTextColor(context.resources.getColor(R.color.black, null))
                tvInterest.setTextColor(context.resources.getColor(R.color.black, null))
                tvPayment.setTextColor(context.resources.getColor(R.color.black, null))
                tvBalance.setTextColor(context.resources.getColor(R.color.black, null))

//                // Tải font tùy chỉnh từ tài nguyên
                val typeface = ResourcesCompat.getFont(context, R.font.montserratlight)
//
//                // Áp dụng font cho TextView
//                textView.typeface = typeface

                tvPrincipal.setTypeface(typeface, Typeface.BOLD)
                tvInterest.setTypeface(typeface, Typeface.BOLD)
                tvPayment.setTypeface(typeface, Typeface.BOLD)
                tvBalance.setTypeface(typeface, Typeface.BOLD)

            }

            tvMonth.text = "${loanPayment.month}"
            tvPrincipal.text = loanPayment.principal.toString()
            tvInterest.text = loanPayment.interest.toString()
            tvPayment.text = loanPayment.payment.toString()
            tvBalance.text = loanPayment.balance.toString()
        }
    }
}
