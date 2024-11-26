package com.example.wms_tindahan.userview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.userview.ItemUserAdapter
import com.example.wms_tindahan.CartItem
import com.example.wms_tindahan.ItemRepository
import com.example.wms_tindahan.R

class UserProductListFragment: Fragment() {
    private lateinit var repository: ItemRepository
    private lateinit var recyclerView: RecyclerView
    private val cartItemList = mutableListOf<CartItem>()
    private lateinit var itemAdapter: ItemUserAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                          savedInstanceState: Bundle?
    ): View? {        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_inventory_userview, container, false)

        // Initialize repository with context
        repository = ItemRepository(requireContext())

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.productsRecyclerView);
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize adapter and set it to RecyclerView
        itemAdapter = ItemUserAdapter(cartItemList)
        recyclerView.adapter = itemAdapter

        // Load items
        loadItems()

        val reviewButton : AppCompatButton = view.findViewById(R.id.reviewOrderButton)

        reviewButton.setOnClickListener{
            val itemsToAdd = itemAdapter.getItemsWithQuantityGreaterThanZero()

        // Create CartItems for the products where quantity > 0
       val cartItemsToAdd = itemsToAdd.map { CartItem(item = it.item, quantity = it.quantity, total_item_price = it.quantity * it.item.price) }
            //var newOrder = NewOrderRequest (items = cartItem)
            println("Cart items to add:")
            cartItemsToAdd.forEach { cartItem ->
                println("Item Name: ${cartItem.item.item_name}, Quantity: ${cartItem.quantity}, Item total: ${cartItem.total_item_price}")
               // var newOrder = NewOrderRequest (items = cartItem)
            }

        // Now you can pass the `cartItemsToAdd` list to wherever you need
        // For example, to create a new order or send to an API
        // Example: postOrder(newOrderRequest)
        //addItemsToCart(cartItemsToAdd)

        }

    return view

    }

    private fun loadItems() {
        Log.d("LoadItems", "onCreate: Activity started")
        repository.getAllItems({ items ->
            // Update the itemList and notify the adapter
            cartItemList.clear()
            val cartItems = items.map { CartItem(it, 0, 0.0) }
            cartItemList.addAll(cartItems)
            itemAdapter.notifyDataSetChanged()  // Update RecyclerView
        }, { error ->
            // Handle error (e.g., show a toast or log the error)
            println("Error fetching items: $error")
        })

    }

    override fun onResume() {
        super.onResume()
        // Refresh the list when the fragment becomes visible again
        loadItems()
    }

    private fun addItemsToCart(cartItems: List<CartItem>) {
        // Your logic to add items to the cart or send them to the server
        // For example, you could call a repository method to place the order
        // postOrder(newOrderRequest)
    }

}