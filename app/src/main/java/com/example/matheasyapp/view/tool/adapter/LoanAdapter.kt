package com.example.matheasyapp.view.tool.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.matheasyapp.R
import com.example.matheasyapp.view.tool.model.LoanPayment
import java.util.*

class LoanAdapter(private val context: Context, private val loanPayments: List<LoanPayment?>) :
    RecyclerView.Adapter<LoanAdapter.ViewHolder>() {

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

//            viewLineTop.visibility = View.GONE
//            viewLineBottom.visibility = View.GONE



            if (position == 0) {
                // first element
                tvMonth.setText("N")
                tvMonth.setTextColor(context.getColor(R.color.header_item))
                tvPrincipal.setText("Vốn gốc")
                tvPrincipal.setTextColor(context.getColor(R.color.header_item))

                tvInterest.setText("Lãi suất")
                tvInterest.setTextColor(context.getColor(R.color.header_item))

                tvPayment.setText("Thanh toán")
                tvPayment.setTextColor(context.getColor(R.color.header_item))

                tvBalance.setText("Số dư")
                tvBalance.setTextColor(context.getColor(R.color.header_item))

//                viewLineTop.visibility = View.GONE

                viewLineBottom.visibility = View.VISIBLE

            }

            if (position == loanPayments.size - 1){
                viewLineTop.visibility = View.VISIBLE

            }


            tvMonth.text = "${loanPayment.month}"
            tvPrincipal.text =  loanPayment.principal.toString()
            tvInterest.text =  loanPayment.interest.toString()
            tvPayment.text = loanPayment.payment.toString()
            tvBalance.text =  loanPayment.balance.toString()
        }
    }
}
