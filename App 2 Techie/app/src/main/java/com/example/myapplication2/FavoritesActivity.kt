package com.example.myapplication2

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
import android.view.animation.AnimationUtils
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
        val tvFavoritesTitle = findViewById<TextView>(R.id.tv_fav_title)
        val logoSmall = findViewById<ImageView>(R.id.logo_small)

        // Highly Visible Advanced Entry animations
        val rotatePopIn = AnimationUtils.loadAnimation(this, R.anim.rotate_pop_in)
        val slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left_overshoot)
        val shake = AnimationUtils.loadAnimation(this, R.anim.shake)

        tvFavoritesTitle?.startAnimation(rotatePopIn)
        logoSmall?.startAnimation(slideInLeft)
        btnSettings?.startAnimation(rotatePopIn)
        
        // Add shaky heading after entrance
        tvFavoritesTitle?.postDelayed({ tvFavoritesTitle.startAnimation(shake) }, 1200)

        btnSettings.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            showSettingsDialog()
        }

        refreshFavorites()

        // Bottom Navigation
        val navItems = listOf(R.id.nav_home, R.id.nav_cart, R.id.nav_profile)
        navItems.forEach { id ->
            findViewById<View>(id)?.setOnClickListener {
                SoundHelper.playClickSound(this)
                it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
                it.postDelayed({
                    val intent = when(id) {
                        R.id.nav_home -> Intent(this, CategoryActivity::class.java)
                        R.id.nav_cart -> Intent(this, CartActivity::class.java)
                        else -> Intent(this, EditProfileActivity::class.java)
                    }
                    startActivity(intent)
                    overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit)
                    finish()
                }, 200)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshFavorites()
    }

    private fun refreshFavorites() {
        val favoritesContainer = findViewById<LinearLayout>(R.id.favorites_container)
        favoritesContainer.removeAllViews()
        
        // Apply staggered animation to the list
        val layoutAnim = AnimationUtils.loadLayoutAnimation(this, R.anim.staggered_entrance)
        favoritesContainer.layoutAnimation = layoutAnim

        val favorites = FavoriteManager.getFavorites()
        if (favorites.isEmpty()) {
            val emptyText = TextView(this).apply {
                text = "No favorites yet!"
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                gravity = Gravity.CENTER
                setPadding(0, 100, 0, 0)
                setTextColor(Color.BLACK)
                startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate_pop_in))
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
                    it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pop_in))
                    FavoriteManager.removeFavorite(product)
                    refreshFavorites()
                    Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
                }

                btnAddToCart.setOnClickListener {
                    SoundHelper.playClickSound(this)
                    it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
                    val priceVal = product.price.replace("AED ", "").toDoubleOrNull() ?: 0.0
                    CartManager.addToCart(CartManager.CartItem(product.name, product.variant, priceVal, product.imageResId))
                    Toast.makeText(this, "${product.name} added to cart!", Toast.LENGTH_SHORT).show()
                    it.postDelayed({ 
                        startActivity(Intent(this, CartActivity::class.java))
                        overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit)
                    }, 200)
                }
                
                itemView.setOnClickListener {
                    it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
                }

                favoritesContainer.addView(itemView)
            }
        }
    }

    private fun showSettingsDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_settings)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        
        val dialogView = dialog.window?.decorView
        dialogView?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_pop_in))

        dialog.findViewById<TextView>(R.id.btn_close_settings).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}