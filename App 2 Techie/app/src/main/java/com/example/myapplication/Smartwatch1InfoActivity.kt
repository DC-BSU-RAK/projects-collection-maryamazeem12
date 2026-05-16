package com.example.myapplication

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.imageview.ShapeableImageView

class Smartwatch1InfoActivity : AppCompatActivity() {

    private var quantity = 0
    private lateinit var tvQuantity: TextView
    private val productName = "Apple Watch SE 3"
    private val productVariant = "Midnight"
    private val productPrice = 1119.00
    private val productImageResId = R.drawable.smartwatch1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smartwatch1_info)

        tvQuantity = findViewById(R.id.tv_quantity)
        val btnPlus = findViewById<ImageView>(R.id.btn_plus)
        val btnMinus = findViewById<ImageView>(R.id.btn_minus)
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnSettings = findViewById<ImageView>(R.id.btn_settings_info)
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
        
        ivProductLarge?.startAnimation(smoothScale)
        tvInfoName?.startAnimation(entrance)
        tvInfoPrice?.startAnimation(entrance)
        tvInfoVariant?.startAnimation(entrance)
        deliveryContainer?.startAnimation(overshootSlide)
        tvDeliveryDays?.startAnimation(overshootSlide)
        ivDetails?.startAnimation(overshootSlide)

        // Initialize quantity from cart if exists
        quantity = CartManager.getQuantity(productName)
        tvQuantity.text = quantity.toString()

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
                CartManager.removeFromCart(productName)
                Toast.makeText(this, "Removed from cart", Toast.LENGTH_SHORT).show()
            }
        }

        btnBack.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            it.postDelayed({ finish() }, 200)
        }

        btnSettings.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            showSettingsDialog()
        }

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navFav = findViewById<ImageView>(R.id.nav_fav)
        val navCart = findViewById<ImageView>(R.id.nav_cart)
        val navProfile = findViewById<ShapeableImageView>(R.id.nav_profile)

        val navItems = listOf(navHome, navFav, navCart, navProfile)
        navItems.forEach { view ->
            view?.setOnClickListener {
                SoundHelper.playClickSound(this)
                view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
                it.postDelayed({
                    val intent = when(view) {
                        navHome -> Intent(this, CategoryActivity::class.java)
                        navFav -> Intent(this, FavoritesActivity::class.java)
                        navCart -> Intent(this, CartActivity::class.java)
                        else -> Intent(this, EditProfileActivity::class.java)
                    }
                    startActivity(intent)
                    finish()
                }, 200)
            }
        }

        // Update profile icon in nav
        val sharedPref = getSharedPreferences("TechiePrefs", MODE_PRIVATE)
        val profilePicResId = sharedPref.getInt("profile_pic", R.drawable.profilepic)
        navProfile?.setImageResource(profilePicResId)
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