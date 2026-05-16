package com.example.app2multipage

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.imageview.ShapeableImageView

class MacbookActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_macbook)

        val ivUserProfile = findViewById<ShapeableImageView>(R.id.iv_user_profile)
        val tvWelcome = findViewById<TextView>(R.id.tv_welcome)
        val btnSettings = findViewById<ImageView>(R.id.btn_settings)
        val navProfile = findViewById<ImageView>(R.id.nav_profile)

        // Load user data
        val sharedPref = getSharedPreferences("TechiePrefs", MODE_PRIVATE)
        val userName = sharedPref.getString("user_name", "Marium")
        val profilePicResId = sharedPref.getInt("profile_pic", R.drawable.profilepic)

        tvWelcome.text = "Welcome! $userName"
        ivUserProfile.setImageResource(profilePicResId)
        navProfile.setImageResource(profilePicResId)

        btnSettings.setOnClickListener {
            showSettingsDialog()
        }

        // Initialize Products
        setupProduct(R.id.product1, "MacBook Air", "Midnight", 4599.00, 4.0f, "600 Reviews", R.drawable.macbookproduct1)
        setupProduct(R.id.product2, "MacBook Neo", "Citrus", 2599.00, 3.0f, "170 Reviews", R.drawable.macbookproduct2)
        setupProduct(R.id.product3, "Apple 13-inch MacBook Air", "Sky blue", 5499.00, 5.0f, "570 Reviews", R.drawable.macbookproduct3)
        setupProduct(R.id.product4, "Apple MacBook Neo ", "Silver", 2599.00, 4.0f, "790 Reviews", R.drawable.macbookproduct4)

        // Category Buttons Navigation
        findViewById<View>(R.id.btn_cat_headset).setOnClickListener {
            startActivity(Intent(this, HeadsetActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        findViewById<View>(R.id.btn_cat_airpods).setOnClickListener {
            startActivity(Intent(this, AirpodsActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        findViewById<View>(R.id.btn_cat_smartwatch).setOnClickListener {
            startActivity(Intent(this, SmartwatchActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        findViewById<View>(R.id.btn_cat_speakers).setOnClickListener {
            startActivity(Intent(this, SpeakerActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // Nav Bar Icons
        findViewById<View>(R.id.nav_home).setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        findViewById<View>(R.id.nav_fav).setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        findViewById<View>(R.id.nav_cart).setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun onResume() {
        super.onResume()
        setupProduct(R.id.product1, "MacBook Air", "Midnight", 4599.00, 4.0f, "600 Reviews", R.drawable.macbookproduct1)
        setupProduct(R.id.product2, "MacBook Neo", "Citrus", 2599.00, 3.0f, "170 Reviews", R.drawable.macbookproduct2)
        setupProduct(R.id.product3, "Apple 13-inch MacBook Air", "Sky blue", 5499.00, 5.0f, "570 Reviews", R.drawable.macbookproduct3)
        setupProduct(R.id.product4, "Apple MacBook Neo ", "Silver", 2599.00, 4.0f, "790 Reviews", R.drawable.macbookproduct4)
    }

    private fun setupProduct(includeId: Int, name: String, variant: String, price: Double, rating: Float, reviews: String, imageResId: Int) {
        val view = findViewById<View>(includeId)
        val ivProduct = view.findViewById<ImageView>(R.id.iv_product_image)
        val tvName = view.findViewById<TextView>(R.id.tv_product_name)
        val tvVariant = view.findViewById<TextView>(R.id.tv_product_variant)
        val tvPrice = view.findViewById<TextView>(R.id.tv_price)
        val ratingBar = view.findViewById<RatingBar>(R.id.rating_bar)
        val tvReviews = view.findViewById<TextView>(R.id.tv_reviews)
        val btnAddToCart = view.findViewById<ImageView>(R.id.btn_add_to_cart)
        val btnFavorite = view.findViewById<ImageView>(R.id.btn_favorite)

        ivProduct.setImageResource(imageResId)
        tvName.text = name
        tvVariant.text = variant
        tvPrice.text = "AED ${String.format("%.2f", price)}"
        ratingBar.rating = rating
        tvReviews.text = reviews

        if (FavoriteManager.isFavorite(name)) {
            btnFavorite.setColorFilter(Color.RED)
        } else {
            btnFavorite.clearColorFilter()
        }

        btnAddToCart.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in))
            CartManager.addToCart(CartManager.CartItem(name, variant, price, imageResId))
            Toast.makeText(this, "$name added to cart!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, CartActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        btnFavorite.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in))
            val nowFav = FavoriteManager.toggleFavorite(
                FavoriteManager.Product(name, variant, "AED ${String.format("%.2f", price)}", rating, reviews, imageResId)
            )
            
            if (nowFav) {
                btnFavorite.setColorFilter(Color.RED)
                Toast.makeText(this, "Added to favorites!", Toast.LENGTH_SHORT).show()
            } else {
                btnFavorite.clearColorFilter()
                Toast.makeText(this, "Removed from favorites!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSettingsDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_settings)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setWindowAnimations(android.R.style.Animation_Dialog)
        dialog.findViewById<TextView>(R.id.btn_close_settings).setOnClickListener { dialog.dismiss() }
        dialog.show()
    }
}