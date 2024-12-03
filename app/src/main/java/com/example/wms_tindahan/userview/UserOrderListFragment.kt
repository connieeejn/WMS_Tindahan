package com.example.wms_tindahan.userview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.userview.OrderUserAdapter
import com.example.wms_tindahan.ApiResponse
import com.example.wms_tindahan.CartItem
import com.example.wms_tindahan.ErrorResponse
import com.example.wms_tindahan.NewOrderRequest
import com.example.wms_tindahan.R
import com.example.wms_tindahan.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserOrderListFragment: Fragment() {

    private lateinit var orderAdapter: OrderUserAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                          savedInstanceState: Bundle?
                            ): View? {
        val view = inflater.inflate(R.layout.fragment_order_userview, container, false)



        val newOrder : NewOrderRequest? = arguments?.getParcelable("cart_items_to_add")
        val cartItems :List<CartItem>?= newOrder?.items

        recyclerView = view.findViewById(R.id.ordersRecyclerView)
        val emptyText: TextView = view.findViewById(R.id.emptyText)
        val emptyCart : ImageView = view.findViewById(R.id.emptyCart)
        val placeOrderButton : AppCompatButton = view.findViewById(R.id.placeOrderButton)

        if (cartItems.isNullOrEmpty() ) {
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

            orderAdapter.notifyDataSetChanged()

            placeOrderButton.setOnClickListener{
                val itemsToAdd = orderAdapter.getItemsWithQuantityGreaterThanZero()

                val cartItemsToAdd = itemsToAdd.map { CartItem(item = it.item, quantity = it.quantity) }
                println(cartItemsToAdd.toString())

                val newOrder = NewOrderRequest (user_id = newOrder.user_id ,items= cartItemsToAdd)

//                Log.d("place order", "USERID: ${newOrder.user_id}")
//                Log.d("place order", cartItemsToAdd.toString())

                placeOrder(newOrder)
            }
        }

    return view

    }


    override fun onResume() {
        super.onResume()
        (activity as? UserDashboard)?.setToolbarTitle("Sari Store")
    }

    private fun placeOrder(newOrder: NewOrderRequest){
        val apiService = RetrofitClient.getInstance(requireContext())

        apiService.placeOrder(newOrder).enqueue(object : Callback<ApiResponse> {

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {

                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()
                    Toast.makeText(requireContext(), apiResponse?.message ?: "Order added successfully!", Toast.LENGTH_LONG).show()

                    val orderTotal = apiResponse?.total_order_price
                    val transactionId = apiResponse?.transaction_id

                    val bundle = Bundle()
                    bundle.putDouble("orderTotal", orderTotal ?: 0.0)  // Ensure you handle nulls safely
                    bundle.putInt("transactionId", transactionId ?: 0)

                    val newFragment = ThankYouFragment()
                    newFragment.arguments = bundle
                    parentFragmentManager.beginTransaction()
                        .replace(com.example.wms_tindahan.R.id.fragment_container, newFragment)
                        .addToBackStack(null)
                        .commit()
                } else {
                    val gson = Gson()
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
                    if (errorResponse != null) {
                        Log.d("error", errorResponse.error)
                        Toast.makeText(requireContext(),  "Error:  ${errorResponse.error}", Toast.LENGTH_LONG).show()
                    }

                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Order posting failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



}