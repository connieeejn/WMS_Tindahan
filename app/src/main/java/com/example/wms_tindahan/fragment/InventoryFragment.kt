package com.example.wms_tindahan.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wms_tindahan.AddNewProduct
import com.example.wms_tindahan.Inventory
import com.example.wms_tindahan.Item
import com.example.wms_tindahan.ItemAdapter
import com.example.wms_tindahan.ItemRepository
import com.example.wms_tindahan.R

class InventoryFragment : Fragment() {

    private lateinit var addBtn: Button
    private lateinit var filterBtn: Button
    private lateinit var categorySpinner: Spinner
    private lateinit var repository: ItemRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: ItemAdapter
    private var itemList: MutableList<Item> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_inventory, container, false)

        // initialize recyclerview
        recyclerView = view.findViewById<RecyclerView>(R.id.productsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        itemAdapter = ItemAdapter(itemList)
        recyclerView.adapter = itemAdapter

        // Initialize repository with context
        repository = ItemRepository(requireContext())


        // fetch items
        loadItems()


        // Test add new item; this is temporary
        // TODO: move add btn to the toolbar
        addBtn = view.findViewById(R.id.addItemButton)


        // Set click listener for the button
        addBtn.setOnClickListener {
            // Redirect to a new activity when the button is clicked
            val intent = Intent(activity, AddNewProduct::class.java)
            startActivity(intent)
        }

        return view
    }


    private fun loadItems() {
        repository.getAllItems({ items ->
            // Update the itemList and notify the adapter
            itemList.clear()
            itemList.addAll(items)
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

        (activity as? Inventory)?.setToolbarTitle("Inventory")
    }


}