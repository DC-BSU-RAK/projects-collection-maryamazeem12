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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {

    private lateinit var tvSubtotal: TextView
    private lateinit var tvShipping: TextView
    private lateinit var tvTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        tvSubtotal = findViewById(R.id.tv_subtotal_value)
        tvShipping = findViewById(R.id.tv_shipping_value)
        tvTotal = findViewById(R.id.tv_total_value)
        val tvCartTitle = findViewById<TextView>(R.id.tv_cart_title)
        val summaryContainer = findViewById<View>(R.id.summary_container)
        val logoSmall = findViewById<ImageView>(R.id.logo_small)
        val btnSettings = findViewById<ImageView>(R.id.btn_settings)

        // Highly Visible Advanced Entry animations
        val rotatePopIn = AnimationUtils.loadAnimation(this, R.anim.rotate_pop_in)
        val slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right_overshoot)
        val slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left_overshoot)
        val staggeredEntrance = AnimationUtils.loadLayoutAnimation(this, R.anim.staggered_entrance)
        
        tvCartTitle.startAnimation(rotatePopIn)
        summaryContainer.startAnimation(slideInRight)
        logoSmall?.startAnimation(slideInLeft)
        btnSettings?.startAnimation(rotatePopIn)

        val rv = findViewById<RecyclerView>(R.id.rv_cart)
        rv.layoutManager = LinearLayoutManager(this)
        
        // Staggered layout animation for cart items
        rv.layoutAnimation = staggeredEntrance

        rv.adapter = CartAdapter(CartManager.getCartItems()) {
            updateTotals()
        }

        updateTotals()

        btnSettings.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            showSettingsDialog()
        }

        findViewById<View>(R.id.btn_checkout).setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            it.postDelayed({
                startActivity(Intent(this, CheckoutActivity::class.java))
                overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit)
            }, 200)
        }

        // Bottom Navigation with animations
        val navItems = listOf(R.id.nav_home, R.id.nav_fav, R.id.nav_profile)
        navItems.forEach { id ->
            findViewById<View>(id).setOnClickListener {
                SoundHelper.playClickSound(this)
                it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
                it.postDelayed({
                    val intent = when(id) {
                        R.id.nav_home -> Intent(this, CategoryActivity::class.java)
                        R.id.nav_fav -> Intent(this, FavoritesActivity::class.java)
                        else -> Intent(this, EditProfileActivity::class.java)
                    }
                    startActivity(intent)
                    overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit)
                    finish()
                }, 200)
            }
        }
    }

    private fun updateTotals() {
        val subtotal = CartManager.getSubtotal()
        val shipping = if (subtotal > 0) 25.0 else 0.0
        val total = subtotal + shipping

        tvSubtotal.text = "AED ${String.format("%.2f", subtotal)}"
        tvShipping.text = "AED ${String.format("%.2f", shipping)}"
        tvTotal.text = "AED ${String.format("%.2f", total)}"
        
        // Dynamic pop animation for total update
        tvTotal.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pop_in))
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