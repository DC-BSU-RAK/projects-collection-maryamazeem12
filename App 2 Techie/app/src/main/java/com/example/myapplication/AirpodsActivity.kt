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

class AirpodsActivity : AppCompatActivity() {

    private lateinit var ivUserProfile: ShapeableImageView
    private lateinit var tvWelcome: TextView
    private lateinit var navProfile: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_airpods)

        ivUserProfile = findViewById(R.id.iv_user_profile)
        tvWelcome = findViewById(R.id.tv_welcome)
        val btnSettings = findViewById<ImageView>(R.id.btn_settings)
        navProfile = findViewById(R.id.nav_profile)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val tvSubtitle = findViewById<TextView>(R.id.tv_subtitle)
        val logoSmall = findViewById<ImageView>(R.id.logo_small)

        // Highly Visible Advanced Entry animations
        val rotatePopIn = AnimationUtils.loadAnimation(this, R.anim.rotate_pop_in)
        val slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left_overshoot)
        val slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right_overshoot)
        val staggeredEntrance = AnimationUtils.loadLayoutAnimation(this, R.anim.staggered_entrance)
        val smoothScale = AnimationUtils.loadAnimation(this, R.anim.smooth_scale_in)
        
        logoSmall?.startAnimation(rotatePopIn)
        btnSettings?.startAnimation(rotatePopIn)
        ivUserProfile.startAnimation(smoothScale)
        tvWelcome.startAnimation(slideInRight)
        tvTitle.startAnimation(slideInLeft)
        tvSubtitle.startAnimation(slideInRight)
        
        // Apply staggered animation to the product list and category buttons
        findViewById<LinearLayout>(R.id.product_list_container)?.layoutAnimation = staggeredEntrance
        findViewById<LinearLayout>(R.id.category_btns_container)?.layoutAnimation = staggeredEntrance

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
                overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit)
            }, 200)
        }

        // Initialize Products
        setupProduct(R.id.product1, "JBL Wave Beam 2 Collection", "Pink", 299.90, 4.0f, "260 Reviews", R.drawable.productairpods1, 1)
        setupProduct(R.id.product2, "Apple AirPods 4", "Pink", 799.90, 3.0f, "140 Reviews", R.drawable.airpodproduct2, 2)
        setupProduct(R.id.product3, "Apple AirPods 4", "Black", 799.90, 5.0f, "500 Reviews", R.drawable.airpodproduct3, 3)
        setupProduct(R.id.product4, "Apple AirPods 4 Teal 16 Edition", "Teal", 799.90, 4.0f, "590 Reviews", R.drawable.airpodproduct4)

        // Category Navigation
        val categories = listOf(R.id.btn_cat_headset, R.id.btn_cat_speakers, R.id.btn_cat_smartwatch, R.id.btn_cat_macbook)
        categories.forEach { id ->
            findViewById<View>(id).setOnClickListener {
                SoundHelper.playClickSound(this)
                it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
                it.postDelayed({
                    val intent = when(id) {
                        R.id.btn_cat_headset -> Intent(this, HeadsetActivity::class.java)
                        R.id.btn_cat_speakers -> Intent(this, SpeakerActivity::class.java)
                        R.id.btn_cat_smartwatch -> Intent(this, SmartwatchActivity::class.java)
                        else -> Intent(this, MacbookActivity::class.java)
                    }
                    startActivity(intent)
                    overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit)
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
                    if (id != R.id.nav_home) {
                        overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit)
                    }
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

    private fun setupProduct(includeId: Int, name: String, variant: String, price: Double, rating: Float, reviews: String, imageResId: Int, productIndex: Int = 0) {
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

        val isFav = FavoriteManager.isFavorite(name)
        btnFavorite.setColorFilter(if (isFav) Color.RED else Color.TRANSPARENT)

        val productClickListener = View.OnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            it.postDelayed({
                val intent = when(productIndex) {
                    1 -> Intent(this, AirpodInfoActivity::class.java)
                    2 -> Intent(this, Airpod2InfoActivity::class.java)
                    3 -> Intent(this, Airpod3InfoActivity::class.java)
                    else -> null
                }
                intent?.let { 
                    startActivity(it)
                    overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit)
                }
            }, 200)
        }
        
        view.setOnClickListener(productClickListener)

        btnAddToCart.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            CartManager.addToCart(CartManager.CartItem(name, variant, price, imageResId))
            Toast.makeText(this, "$name added to cart!", Toast.LENGTH_SHORT).show()
            it.postDelayed({ 
                startActivity(Intent(this, CartActivity::class.java))
                overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit)
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
        dialogView?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_pop_in))

        dialog.findViewById<TextView>(R.id.btn_close_settings)?.setOnClickListener {
            SoundHelper.playClickSound(this)
            dialog.dismiss()
        }
        dialog.show()
    }
}