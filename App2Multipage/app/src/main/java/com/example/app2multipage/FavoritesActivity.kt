package com.example.app2multipage

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FavoritesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val btnSettings = findViewById<ImageView>(R.id.btn_settings)

        btnSettings.setOnClickListener {
            SoundHelper.playClickSound(this)
            showSettingsDialog()
        }

        refreshFavorites()

        findViewById<View>(R.id.nav_home).setOnClickListener {
            SoundHelper.playClickSound(this)
            startActivity(Intent(this, CategoryActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }
        
        findViewById<View>(R.id.nav_cart).setOnClickListener {
            SoundHelper.playClickSound(this)
            startActivity(Intent(this, CartActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        findViewById<View>(R.id.nav_profile).setOnClickListener {
            SoundHelper.playClickSound(this)
            Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshFavorites()
    }

    private fun refreshFavorites() {
        val favoritesContainer = findViewById<LinearLayout>(R.id.favorites_container)
        favoritesContainer.removeAllViews()

        val favorites = FavoriteManager.getFavorites()
        if (favorites.isEmpty()) {
            val emptyText = TextView(this).apply {
                text = "No favorites yet!"
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                gravity = Gravity.CENTER
                setPadding(0, 100, 0, 0)
                setTextColor(Color.BLACK)
            }
            favoritesContainer.addView(emptyText)
        } else {
            for (product in favorites) {
                val itemView = LayoutInflater.from(this).inflate(R.layout.item_product, favoritesContainer, false)
                
                val ivProduct = itemView.findViewById<ImageView>(R.id.iv_product_image)
                val tvName = itemView.findViewById<TextView>(R.id.tv_product_name)
                val tvVariant = itemView.findViewById<TextView>(R.id.tv_product_variant)
                val tvPrice = itemView.findViewById<TextView>(R.id.tv_price)
                val ratingBar = itemView.findViewById<RatingBar>(R.id.rating_bar)
                val tvReviews = itemView.findViewById<TextView>(R.id.tv_reviews)
                val btnAddToCart = itemView.findViewById<ImageView>(R.id.btn_add_to_cart)
                val btnFavorite = itemView.findViewById<ImageView>(R.id.btn_favorite)

                ivProduct.setImageResource(product.imageResId)
                tvName.text = product.name
                tvVariant.text = product.variant
                tvPrice.text = product.price
                ratingBar.rating = product.rating
                tvReviews.text = product.reviews
                
                btnFavorite.setColorFilter(Color.RED)
                btnFavorite.setOnClickListener {
                    SoundHelper.playClickSound(this)
                    FavoriteManager.removeFavorite(product)
                    refreshFavorites()
                    Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
                }

                btnAddToCart.setOnClickListener {
                    SoundHelper.playClickSound(this)
                    val priceVal = product.price.replace("AED ", "").toDoubleOrNull() ?: 0.0
                    CartManager.addToCart(CartManager.CartItem(product.name, product.variant, priceVal, product.imageResId))
                    Toast.makeText(this, "${product.name} added to cart!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, CartActivity::class.java))
                }

                favoritesContainer.addView(itemView)
            }
        }
    }

    private fun showSettingsDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_settings)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setWindowAnimations(android.R.style.Animation_Dialog)
        dialog.findViewById<TextView>(R.id.btn_close_settings).setOnClickListener {
            SoundHelper.playClickSound(this)
            dialog.dismiss()
        }
        dialog.show()
    }
}