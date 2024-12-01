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
import com.example.wms_tindahan.NewOrderRequest
import com.example.wms_tindahan.R



class UserProductListFragment: Fragment() {
    private lateinit var itemRepository: ItemRepository
    private lateinit var recyclerView: RecyclerView
    private val cartItemList = mutableListOf<CartItem>()
    private lateinit var itemAdapter: ItemUserAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                          savedInstanceState: Bundle?
    ): View? {        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_inventory_userview, container, false)

        //Retrieve userID
        val userId = arguments?.getString("USER_ID") ?: activity?.intent?.getStringExtra("USER_ID") ?: "Default ID"

        // Initialize repository with context
        itemRepository = ItemRepository(requireContext())

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
       val cartItemsToAdd = itemsToAdd.map { CartItem(item = it.item, quantity = it.quantity) }
           // println(cartItemsToAdd.toString())

            val newOrder = NewOrderRequest (user_id = userId.toInt(),items= cartItemsToAdd)

                val newFragment = UserOrderListFragment() // The fragment to navigate to
                val bundle = Bundle()
                bundle.putParcelable("cart_items_to_add", newOrder)

                println(newOrder)

                newFragment.arguments = bundle

                parentFragmentManager.beginTransaction() // Use parentFragmentManager
                    .replace(com.example.wms_tindahan.R.id.fragment_container, newFragment) // Replace current fragment
                    .addToBackStack(null) // Add to back stack for navigation
                    .commit()
        }

    return view

    }

    private fun loadItems() {
        Log.d("LoadItems", "onCreate: Activity started")
        itemRepository.getAllItems({ items ->
            // Update the itemList and notify the adapter
            cartItemList.clear()
            val cartItems = items.map { CartItem(it, 0) }
            cartItemList.addAll(cartItems)
            Log.d("LoadItems", cartItems.toString())

            itemAdapter.notifyDataSetChanged()  // Update RecyclerView
        }, { error ->
            // Handle error (e.g., show a toast or log the error)
            println("Error fetching items: $error")
        })

    }

    override fun onResume() {
        super.onResume()
        loadItems()
        (activity as? UserDashboard)?.setToolbarTitle("Sari Store")

    }



}