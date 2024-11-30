package com.example.wms_tindahan.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Filter
import android.widget.ImageView
import android.widget.LinearLayout
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

    private lateinit var addBtn: ImageView
    private lateinit var filterBtn: ImageView
    private lateinit var categorySpinner: Spinner
    private lateinit var repository: ItemRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: ItemAdapter
    private var itemList: MutableList<Item> = mutableListOf()
    private var categories: MutableList<String> = mutableListOf()

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

        // filter button
        filterBtn = view.findViewById(R.id.filterBtn)
        filterBtn.setOnClickListener {
            val categoryContainer: LinearLayout = view.findViewById(R.id.inventoryFilterBtnLayout)
            if(categoryContainer.visibility == View.VISIBLE) {
                categoryContainer.visibility = View.GONE
            } else {
                categoryContainer.visibility = View.VISIBLE
            }
        }

        // category filter
        categorySpinner = view.findViewById(R.id.categorySpinner)

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

            // iterate to get categories
            loadCategorySpinner(itemList)
        }, { error ->
            // Handle error (e.g., show a toast or log the error)
            println("Error fetching items: $error")
        })
    }

    private fun loadCategorySpinner(itemList: List<Item>) {
        // clear categories to avoid duplicates
        categories.clear()

        // add "All" category firs
        categories.add("All")

        // add distinct categories from item list
        val distinctCategories = itemList.map { it.category }.distinct()
        categories.addAll(distinctCategories)
        Log.d("Categories", categories.toString())

        // set up spinner adapter
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = spinnerAdapter

        // handle spinner item selection
        categorySpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
               Log.d("SELECTED CATEGORY", position.toString() )
                val selectedCategory = parent?.getItemAtPosition(position).toString()
                var filteredItems: List<Item> = mutableListOf()

                filteredItems = when(selectedCategory) {
                    "All" -> itemList
                    else -> itemList.filter { it.category == selectedCategory }}


                Log.d("Filtered Categories", filteredItems.toString())


                updateRecyclerView(filteredItems)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun updateRecyclerView(filteredItems: List<Item>) {
        itemAdapter.updateData(filteredItems)
    }


    override fun onResume() {

        super.onResume()
        // Refresh the list when the fragment becomes visible again
        loadItems()
    }
}