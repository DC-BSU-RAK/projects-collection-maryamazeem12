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

class HeadsetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_headset)

        val ivUserProfile = findViewById<ShapeableImageView>(R.id.iv_user_profile)
        val tvWelcome = findViewById<TextView>(R.id.tv_welcome)
        val btnSettings = findViewById<ImageView>(R.id.btn_settings)
        val navProfile = findViewById<ImageView>(R.id.nav_profile)

        val sharedPref = getSharedPreferences("TechiePrefs", MODE_PRIVATE)
        val userName = sharedPref.getString("user_name", "Marium")
        val profilePicResId = sharedPref.getInt("profile_pic", R.drawable.profilepic)

        tvWelcome.text = "Welcome! $userName"
        ivUserProfile.setImageResource(profilePicResId)
        navProfile.setImageResource(profilePicResId)

        btnSettings.setOnClickListener {
            SoundHelper.playClickSound(this)
            showSettingsDialog()
        }

        setupProduct(R.id.product1, "Airbuds Max Wireless", "White", 129.90, 3.0f, "100 Reviews", R.drawable.headsetproduct1)
        setupProduct(R.id.product2, "AirPods Max Wireless Headset", "Sky blue", 1799.00, 4.0f, "200 Reviews", R.drawable.headsetproduct2)
        setupProduct(R.id.product3, "JBL Tune Wireless Headset", "Purple", 199.00, 5.0f, "570 Reviews", R.drawable.headsetproduct3)
        setupProduct(R.id.product4, "Beats Studio Wireless Headset", "Sandstone", 765.00, 4.0f, "590 Reviews", R.drawable.headsetproduct4)

        findViewById<View>(R.id.btn_cat_airpods).setOnClickListener {
            SoundHelper.playClickSound(this)
            startActivity(Intent(this, AirpodsActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        findViewById<View>(R.id.btn_cat_speakers).setOnClickListener {
            SoundHelper.playClickSound(this)
            startActivity(Intent(this, SpeakerActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        findViewById<View>(R.id.btn_cat_smartwatch).setOnClickListener {
            SoundHelper.playClickSound(this)
            startActivity(Intent(this, SmartwatchActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        findViewById<View>(R.id.btn_cat_macbook).setOnClickListener {
            SoundHelper.playClickSound(this)
            startActivity(Intent(this, MacbookActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        findViewById<View>(R.id.nav_home).setOnClickListener {
            SoundHelper.playClickSound(this)
            startActivity(Intent(this, CategoryActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }
        findViewById<View>(R.id.nav_fav).setOnClickListener {
            SoundHelper.playClickSound(this)
            startActivity(Intent(this, FavoritesActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        findViewById<View>(R.id.nav_cart).setOnClickListener {
            SoundHelper.playClickSound(this)
            startActivity(Intent(this, CartActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun onResume() {
        super.onResume()
        setupProduct(R.id.product1, "Airbuds Max Wireless", "White", 129.90, 3.0f, "100 Reviews", R.drawable.headsetproduct1)
        setupProduct(R.id.product2, "AirPods Max Wireless Headset", "Sky blue", 1799.00, 4.0f, "200 Reviews", R.drawable.headsetproduct2)
        setupProduct(R.id.product3, "JBL Tune Wireless Headset", "Purple", 199.00, 5.0f, "570 Reviews", R.drawable.headsetproduct3)
        setupProduct(R.id.product4, "Beats Studio Wireless Headset", "Sandstone", 765.00, 4.0f, "590 Reviews", R.drawable.headsetproduct4)
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
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in))
            CartManager.addToCart(CartManager.CartItem(name, variant, price, imageResId))
            Toast.makeText(this, "$name added to cart!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, CartActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        btnFavorite.setOnClickListener {
            SoundHelper.playClickSound(this)
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
        dialog.findViewById<TextView>(R.id.btn_close_settings).setOnClickListener {
            SoundHelper.playClickSound(this)
            dialog.dismiss()
        }
        dialog.show()
    }
}