package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.imageview.ShapeableImageView

class SmartwatchActivity : AppCompatActivity() {

    private lateinit var ivUserProfile: ShapeableImageView
    private lateinit var tvWelcome: TextView
    private lateinit var navProfile: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smartwatch)

        ivUserProfile = findViewById(R.id.iv_user_profile)
        tvWelcome = findViewById(R.id.tv_welcome)
        val btnSettings = findViewById<ImageView>(R.id.btn_settings)
        navProfile = findViewById(R.id.nav_profile)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val tvSubtitle = findViewById<TextView>(R.id.tv_subtitle)

        // Advanced Entry animations
        val entrance = AnimationUtils.loadAnimation(this, R.anim.entrance_anim)
        val smoothScale = AnimationUtils.loadAnimation(this, R.anim.smooth_scale_in)
        val overshootSlide = AnimationUtils.loadAnimation(this, R.anim.overshoot_slide_in)
        
        ivUserProfile.startAnimation(smoothScale)
        tvWelcome.startAnimation(entrance)
        tvTitle?.startAnimation(overshootSlide)
        tvSubtitle?.startAnimation(entrance)

        // Apply staggered animation to the product list and category buttons
        val layoutAnim = AnimationUtils.loadLayoutAnimation(this, R.anim.staggered_entrance)
        findViewById<LinearLayout>(R.id.product_list_container)?.layoutAnimation = layoutAnim
        findViewById<LinearLayout>(R.id.category_btns_container)?.layoutAnimation = layoutAnim

        refreshUserData()

        btnSettings.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            showSettingsDialog()
        }

        ivUserProfile.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            it.postDelayed({
                startActivity(Intent(this, EditProfileActivity::class.java))
            }, 200)
        }

        setupProduct(R.id.product1, "Apple Watch SE 3", "Midnight", 1119.00, 4.0f, "560 Reviews", R.drawable.smartwatchproduct1, Smartwatch1InfoActivity::class.java)
        setupProduct(R.id.product2, "Maxwell Health Smartwatch", "Pink", 435.00, 3.0f, "130 Reviews", R.drawable.smartwatchproduct2, Smartwatch2InfoActivity::class.java)
        setupProduct(R.id.product3, "Apple Watch Series 11", "Purple", 1599.00, 3.0f, "170 Reviews", R.drawable.smartwatchproduct3)
        setupProduct(R.id.product4, "Apple Watch Ultra 3", "Natural", 799.90, 4.5f, "780 Reviews", R.drawable.smartwatchproduct4)

        val categories = listOf(R.id.btn_cat_headset, R.id.btn_cat_airpods, R.id.btn_cat_speakers, R.id.btn_cat_macbook)
        categories.forEach { id ->
            findViewById<View>(id)?.setOnClickListener {
                SoundHelper.playClickSound(this)
                it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
                it.postDelayed({
                    val intent = when(id) {
                        R.id.btn_cat_headset -> Intent(this, HeadsetActivity::class.java)
                        R.id.btn_cat_airpods -> Intent(this, AirpodsActivity::class.java)
                        R.id.btn_cat_speakers -> Intent(this, SpeakerActivity::class.java)
                        else -> Intent(this, MacbookActivity::class.java)
                    }
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }, 200)
            }
        }

        // Bottom Navigation
        val navItems = listOf(R.id.nav_home, R.id.nav_fav, R.id.nav_cart, R.id.nav_profile)
        navItems.forEach { id ->
            findViewById<View>(id).setOnClickListener {
                SoundHelper.playClickSound(this)
                it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
                it.postDelayed({
                    val intent = when(id) {
                        R.id.nav_home -> Intent(this, CategoryActivity::class.java)
                        R.id.nav_fav -> Intent(this, FavoritesActivity::class.java)
                        R.id.nav_cart -> Intent(this, CartActivity::class.java)
                        else -> Intent(this, EditProfileActivity::class.java)
                    }
                    startActivity(intent)
                    finish()
                }, 200)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshUserData()
    }

    private fun refreshUserData() {
        val sharedPref = getSharedPreferences("TechiePrefs", MODE_PRIVATE)
        val userName = sharedPref.getString("user_name", "User")
        val profilePicResId = sharedPref.getInt("profile_pic", R.drawable.profilepic)

        tvWelcome.text = "Welcome! $userName"
        ivUserProfile.setImageResource(profilePicResId)
        navProfile.setImageResource(profilePicResId)
    }

    private fun setupProduct(includeId: Int, name: String, variant: String, price: Double, rating: Float, reviews: String, imageResId: Int, targetActivity: Class<*>? = null) {
        val view = findViewById<View>(includeId) ?: return
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

        if (targetActivity != null) {
            val clickListener = View.OnClickListener {
                SoundHelper.playClickSound(this)
                it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
                it.postDelayed({ 
                    startActivity(Intent(this, targetActivity))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }, 200)
            }
            view.setOnClickListener(clickListener)
        }

        val isFav = FavoriteManager.isFavorite(name)
        btnFavorite.setColorFilter(if (isFav) Color.RED else Color.TRANSPARENT)

        btnAddToCart.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            CartManager.addToCart(CartManager.CartItem(name, variant, price, imageResId))
            Toast.makeText(this, "$name added to cart!", Toast.LENGTH_SHORT).show()
            it.postDelayed({ 
                startActivity(Intent(this, CartActivity::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }, 200)
        }

        btnFavorite.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pop_in))
            val nowFav = FavoriteManager.toggleFavorite(FavoriteManager.Product(name, variant, "AED ${String.format("%.2f", price)}", rating, reviews, imageResId))
            btnFavorite.setColorFilter(if (nowFav) Color.RED else Color.TRANSPARENT)
        }
    }

    private fun showSettingsDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_settings)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        
        val dialogView = dialog.window?.decorView
        dialogView?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.entrance_anim))

        dialog.findViewById<TextView>(R.id.btn_close_settings).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}