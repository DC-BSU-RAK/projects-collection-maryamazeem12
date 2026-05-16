package com.example.myapplication2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AirpodInfoActivity : AppCompatActivity() {

    private var quantity = 0
    private val productName = "JBL Wave Beam 2 Collection"
    private val productVariant = "Pink"
    private val productPrice = 299.90
    private val productImageResId = R.drawable.productairpods1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_airpod_info)

        val tvQuantity = findViewById<TextView>(R.id.tv_quantity)
        val btnPlus = findViewById<ImageView>(R.id.btn_plus)
        val btnMinus = findViewById<ImageView>(R.id.btn_minus)
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val navProfile = findViewById<ImageView>(R.id.nav_profile)
        val ivProductLarge = findViewById<ImageView>(R.id.iv_product_large)
        val tvInfoName = findViewById<TextView>(R.id.tv_info_name)
        val tvInfoPrice = findViewById<TextView>(R.id.tv_info_price)
        val tvInfoVariant = findViewById<TextView>(R.id.tv_info_variant)
        val deliveryContainer = findViewById<View>(R.id.delivery_container)
        val tvDeliveryDays = findViewById<TextView>(R.id.tv_delivery_days)
        val ivDetails = findViewById<ImageView>(R.id.iv_details)

        // Advanced Entry animations
        val entrance = AnimationUtils.loadAnimation(this, R.anim.entrance_anim)
        val smoothScale = AnimationUtils.loadAnimation(this, R.anim.smooth_scale_in)
        val overshootSlide = AnimationUtils.loadAnimation(this, R.anim.overshoot_slide_in)
        val shake = AnimationUtils.loadAnimation(this, R.anim.shake)
        
        ivProductLarge.startAnimation(smoothScale)
        tvInfoName.startAnimation(entrance)
        tvInfoPrice.startAnimation(entrance)
        tvInfoVariant.startAnimation(entrance)
        deliveryContainer.startAnimation(overshootSlide)
        tvDeliveryDays.startAnimation(overshootSlide)
        ivDetails.startAnimation(overshootSlide)
        
        // Add shaky heading after entrance
        tvInfoName.postDelayed({ tvInfoName.startAnimation(shake) }, 1200)

        tvQuantity.text = quantity.toString()

        // Refresh user data for nav
        val sharedPref = getSharedPreferences("TechiePrefs", MODE_PRIVATE)
        val profilePicResId = sharedPref.getInt("profile_pic", R.drawable.profilepic)
        navProfile.setImageResource(profilePicResId)

        btnBack.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            it.postDelayed({ finish() }, 200)
        }

        btnPlus.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            quantity++
            tvQuantity.text = quantity.toString()
            CartManager.addToCart(CartManager.CartItem(productName, productVariant, productPrice, productImageResId))
            Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show()
        }

        btnMinus.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            if (quantity > 0) {
                quantity--
                tvQuantity.text = quantity.toString()
                val cartItem = CartManager.getCartItems().find { it.name == productName && it.variant == productVariant }
                if (cartItem != null) {
                    if (cartItem.quantity > 1) {
                        cartItem.quantity--
                    } else {
                        CartManager.removeItem(cartItem)
                    }
                    Toast.makeText(this, "Removed from cart", Toast.LENGTH_SHORT).show()
                }
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
}