package com.example.wms_tindahan.userview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.wms_tindahan.R

class ThankYouFragment: Fragment() {

    private var orderTotal: Double = 0.0
    private var transactionId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.thank_you, container, false)

        arguments?.let { bundle ->
            orderTotal = bundle.getDouble("orderTotal", 0.0)
            transactionId = bundle.getInt("transactionId", 0) ?: 0
        }

        val textViewOrderTotal: TextView = view.findViewById(R.id.orderTotal)
        val textViewTransactionId: TextView = view.findViewById(R.id.orderId)

        textViewOrderTotal.text = "Order Total: $${"%.2f".format(orderTotal)}"
        textViewTransactionId.text = "Transaction ID: $transactionId"


        return view
    }
}