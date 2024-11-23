package com.example.wms_tindahan

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddNewProduct : AppCompatActivity() {
    private lateinit var repository: ItemRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_new_product)

        // Initialize repository with context
        repository = ItemRepository(this)


        val nameInput: EditText = findViewById(R.id.prodNameEditTxt)
        val descInput: EditText = findViewById(R.id.prodDescEditTxt)
        val catInput: EditText = findViewById(R.id.prodCategoryEditTxt)
        val priceInput: EditText = findViewById(R.id.prodPriceEditTxt)
        val qtyInput: EditText = findViewById(R.id.prodQtyEditTxt)


        val addBtn: Button = findViewById(R.id.addButton)

        addBtn.setOnClickListener {
            val name = nameInput.text.toString()
            val description = descInput.text.toString()
            val category = catInput.text.toString()
            val price = priceInput.text.toString().toDouble()
            val stockQty = qtyInput.text.toString().toInt()

            if (name.isNotEmpty() &&
                description.isNotEmpty() &&
                category.isNotEmpty() &&
                price != null &&
                stockQty != null) {

                val newItem = NewItemRequest(
                    item_name = name,
                    item_description = description,
                    category = category,
                    price = price,
                    stock_quantity = stockQty
                    )


                repository.addItem(newItem,
                    onSuccess = {
                        Toast.makeText(this, "Item added successfully!", Toast.LENGTH_LONG).show()
                        finish()  // Go back to the main activity
                    },
                    onError = {
                        Toast.makeText(this, "Error: $it", Toast.LENGTH_LONG).show()
                    })
            } else {
                Toast.makeText(this, "Please enter all required fields", Toast.LENGTH_LONG).show()
            }

        }

    }
}