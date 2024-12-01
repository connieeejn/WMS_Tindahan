package com.example.wms_tindahan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

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
        val imgInput: EditText = findViewById(R.id.prodImgEditTxt)
        val addBtn: Button = findViewById(R.id.addButton)
        val title: TextView = findViewById(R.id.AddNewProductTitle)

        // TODO: add image
        // handle edit item
        // get intent values from product
        val productId = intent.getIntExtra("product_id", 0)
        val productName = intent.getStringExtra("product_name")
        val productDescription = intent.getStringExtra("product_description")
        val productPrice = intent.getDoubleExtra("product_price", 0.0)
        val productQty = intent.getIntExtra("product_qty", 0)
        val productCategory = intent.getStringExtra("product_category")
        val productImage = intent.getStringExtra("product_image")



        // TODO: add image
        if(productId != 0
            && productName != null
            && productDescription != null
            && productPrice != 0.0
            && productQty != 0
            && productCategory != null
            && productImage != null) {


            // populate input fields
            nameInput.setText(productName)
            descInput.setText(productDescription)
            priceInput.setText(productPrice.toString())
            qtyInput.setText(productQty.toString())
            catInput.setText(productCategory)
            imgInput.setText(productImage)

            // change button name to update/edit
            addBtn.text = "Update Product"
            title.text = "Edit Product"
        }

        Log.d("EDIT ID", "ID: ${productId}")


        addBtn.setOnClickListener {
            val name = nameInput.text.toString()
            val description = descInput.text.toString()
            val category = catInput.text.toString()
            val price = priceInput.text.toString().toDouble()
            val stockQty = qtyInput.text.toString().toInt()
            val imgUrl = imgInput.text.toString()




            if (name.isNotEmpty() &&
                description.isNotEmpty() &&
                category.isNotEmpty() &&
                price != 0.0 &&
                stockQty != 0 &&
                imgUrl.isNotEmpty()) {



                // Check if it's an update or add operation based on productId
                if (productId != 0) {
                    // Handle updating an existing product
                    val updatedItem = Item(
                        id = productId,
                        item_name = nameInput.text.toString(),
                        item_description = descInput.text.toString(),
                        category = catInput.text.toString(),
                        price = priceInput.text.toString().toDouble(),
                        stock_quantity = qtyInput.text.toString().toInt(),
                        image = imgInput.text.toString()
                    )

                    repository.updateItem(
                        productId, updatedItem,  // Pass productId for the item to be updated
                        onSuccess = {
                            Toast.makeText(this, "Item updated successfully!", Toast.LENGTH_LONG).show()

//                            val intent = Intent(this, Product::class.java)

                            // Pass the product data to the Product activity
                            val resultIntent = Intent()

                            resultIntent.putExtra("product_id", updatedItem.id)
                            resultIntent.putExtra("product_name", updatedItem.item_name)
                            resultIntent.putExtra("product_description", updatedItem.item_description)
                            resultIntent.putExtra("product_price", updatedItem.price)
                            resultIntent.putExtra("product_qty", updatedItem.stock_quantity)
                            resultIntent.putExtra("product_category", updatedItem.category)
                            resultIntent.putExtra("product_image", updatedItem.image)

                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()
                        },
                        onError = {
                            Toast.makeText(this, "Error: $it", Toast.LENGTH_LONG).show()
                        })
                } else {
                    // Handle adding a new product
                    val newItem = NewItemRequest(
                        item_name = name,
                        item_description = description,
                        category = category,
                        price = price,
                        stock_quantity = stockQty,
                        image = imgUrl
                    )

                    repository.addItem(newItem,
                        onSuccess = {
                            Toast.makeText(this, "Item added successfully!", Toast.LENGTH_LONG).show()
                            finish()  // Go back to the main activity
                        },
                        onError = {
                            Toast.makeText(this, "Error: $it", Toast.LENGTH_LONG).show()
                        })
                }}


           else {
                Toast.makeText(this, "Please enter all required fields", Toast.LENGTH_LONG).show()
            }

        }

    }
}