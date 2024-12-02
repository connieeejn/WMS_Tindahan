package com.example.wms_tindahan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(
    private val transactions: List<Transaction>,
    private val onItemClick: (Transaction) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.bind(transaction)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val transactionId: TextView = itemView.findViewById(R.id.transactionId)
        private val orderDate: TextView = itemView.findViewById(R.id.orderDate)
        private val userId: TextView = itemView.findViewById(R.id.userId)
        private val totalOrderPrice: TextView = itemView.findViewById(R.id.totalOrderPrice)


        fun bind(transaction: Transaction) {
            transactionId.text = "Transaction ID: ${transaction.transaction_id}"
            orderDate.text = "Date: ${transaction.order_date}"
            userId.text = "User ID: ${transaction.user_id}"
            totalOrderPrice.text = String.format("$ %.2f", transaction.total_order_price)
            val totalItemQuantity = transaction.items.size
            itemView.findViewById<TextView>(R.id.transactionItemQty).text = " Items: $totalItemQuantity"
        }
    }
}
