package com.example.wms_tindahan.userview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.userview.ItemUserAdapter
import com.example.userview.OrderUserAdapter
import com.example.wms_tindahan.CartItem
import com.example.wms_tindahan.Item
import com.example.wms_tindahan.NewOrderRequest
import com.example.wms_tindahan.OrderItem
import com.example.wms_tindahan.R
import com.example.wms_tindahan.Transaction


class UserOrderListFragment: Fragment() {

    private lateinit var orderAdapter: OrderUserAdapter
  //  private lateinit var orderRepository: OrderRepository
    //private lateinit var recyclerView: RecyclerView
   // private val orderData : NewOrderRequest

   // private val cartItemsToAdd = mutableListOf<CartItem>()
    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                          savedInstanceState: Bundle?
                            ): View? {
        val view = inflater.inflate(R.layout.fragment_order_userview, container, false)

       // orderRepository = OrderRepository(requireContext())

        val newOrder : NewOrderRequest? = arguments?.getParcelable("cart_items_to_add")

        recyclerView = view.findViewById(R.id.ordersRecyclerView)
        val emptyText: TextView = view.findViewById(R.id.emptyText)
        val emptyCart : ImageView = view.findViewById(R.id.emptyCart)
        val placeOrderButton : AppCompatButton = view.findViewById(R.id.placeOrderButton)

        if (newOrder == null ) {
            // Show the empty state text and hide RecyclerView
            emptyText.visibility = View.VISIBLE
            emptyCart.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            placeOrderButton.visibility = View.GONE
        } else {
            // Populate the list and show RecyclerView
            emptyText.visibility = View.GONE
            emptyCart.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            placeOrderButton.visibility = View.VISIBLE

            // Initialize adapter and set it to RecyclerView
            val cartItemsToAdd = newOrder.items

            orderAdapter = OrderUserAdapter(cartItemsToAdd)
            recyclerView.adapter = orderAdapter

            //Code below is needed to populate the recyclerview
            // orderList.clear()
            // orderList.add(cartItemsToAdd)

            orderAdapter.notifyDataSetChanged()
        }

    return view

    }


    override fun onResume() {
        super.onResume()
        (activity as? UserDashboard)?.setToolbarTitle("Sari Store")
    }



}