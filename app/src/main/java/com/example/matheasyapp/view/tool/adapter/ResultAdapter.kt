package com.example.matheasyapp.view.tool.adapter

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
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

            viewLineTop.visibility = View.GONE
            viewLineBottom.visibility = View.GONE


            if (position == 0) {
                // first element
                viewLineTop.visibility = View.GONE
                viewLineBottom.visibility = View.VISIBLE

                tvMonth.setTextColor(context.resources.getColor(R.color.header_item,null))
                tvPrincipal.setTextColor(context.resources.getColor(R.color.header_item, null))
                tvInterest.setTextColor(context.resources.getColor(R.color.header_item, null))
                tvPayment.setTextColor(context.resources.getColor(R.color.header_item, null))
                tvBalance.setTextColor(context.resources.getColor(R.color.header_item, null))

                // Xử lý cho phần tử đầu tiên
                tvMonth.text = "N"
                tvPrincipal.text = loanPayment.principal.toString()
                tvInterest.text = loanPayment.interest.toString()
                tvPayment.text = loanPayment.payment.toString()
                tvBalance.text = loanPayment.balance.toString()

            } else if (position == loanPayments.size - 1) {
                // last element

                viewLineTop.visibility = View.VISIBLE
                viewLineBottom.visibility = View.GONE

                tvMonth.setTextColor(context.resources.getColor(R.color.currency, null))

                tvPrincipal.setTextColor(context.resources.getColor(R.color.currency, null))
                tvInterest.setTextColor(context.resources.getColor(R.color.currency, null))
                tvPayment.setTextColor(context.resources.getColor(R.color.currency, null))
                tvBalance.setTextColor(context.resources.getColor(R.color.currency, null))

                val typeface = ResourcesCompat.getFont(context, R.font.inter_28pt_light)
                tvPrincipal.setTypeface(typeface, Typeface.BOLD)
                tvInterest.setTypeface(typeface, Typeface.BOLD)
                tvPayment.setTypeface(typeface, Typeface.BOLD)
                tvBalance.setTypeface(typeface, Typeface.BOLD)

                val drawable = ContextCompat.getDrawable(context, R.drawable.ic_sigma)
                tvMonth.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
                tvMonth.gravity = Gravity.CENTER
                tvMonth.compoundDrawablePadding = 8 // padding between text and drawable in pixels
                tvMonth.text = ""
                tvPrincipal.text = loanPayment.principal.toString()
                tvInterest.text = loanPayment.interest.toString()
                tvPayment.text = loanPayment.payment.toString()
                tvBalance.text = loanPayment.balance.toString()

            } else {
                // middle elements (not first, not last)
                viewLineTop.visibility = View.GONE
                viewLineBottom.visibility = View.GONE

                tvMonth.setTextColor(context.resources.getColor(R.color.currency, null))

                tvPrincipal.setTextColor(context.resources.getColor(R.color.currency, null))
                tvInterest.setTextColor(context.resources.getColor(R.color.currency, null))
                tvPayment.setTextColor(context.resources.getColor(R.color.currency, null))
                tvBalance.setTextColor(context.resources.getColor(R.color.currency, null))

                val typeface = ResourcesCompat.getFont(context, R.font.inter_28pt_light)
                tvPrincipal.setTypeface(typeface, Typeface.NORMAL)
                tvInterest.setTypeface(typeface, Typeface.NORMAL)
                tvPayment.setTypeface(typeface, Typeface.NORMAL)
                tvBalance.setTypeface(typeface, Typeface.NORMAL)

                tvMonth.gravity = Gravity.CENTER
                tvMonth.text = "${loanPayment.month}"
                tvMonth.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                tvPrincipal.text = loanPayment.principal.toString()
                tvInterest.text = loanPayment.interest.toString()
                tvPayment.text = loanPayment.payment.toString()
                tvBalance.text = loanPayment.balance.toString()
            }


        }
    }
}
