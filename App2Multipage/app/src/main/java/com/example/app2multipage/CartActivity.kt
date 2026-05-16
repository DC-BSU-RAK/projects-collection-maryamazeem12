package com.example.app2multipage

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
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

        val rv = findViewById<RecyclerView>(R.id.rv_cart)
        rv.layoutManager = LinearLayoutManager(this)

        rv.adapter = CartAdapter(CartManager.getCartItems()) {
            updateTotals()
        }

        updateTotals()

        findViewById<View>(R.id.btn_settings).setOnClickListener {
            SoundHelper.playClickSound(this)
            showSettingsDialog()
        }

        findViewById<View>(R.id.btn_checkout).setOnClickListener {
            SoundHelper.playClickSound(this)
            startActivity(Intent(this, CheckoutActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        findViewById<View>(R.id.nav_home).setOnClickListener {
            SoundHelper.playClickSound(this)
            finish()
        }

        findViewById<View>(R.id.nav_fav).setOnClickListener {
            SoundHelper.playClickSound(this)
            startActivity(Intent(this, FavoritesActivity::class.java))
        }
    }

    private fun updateTotals() {
        val subtotal = CartManager.getSubtotal()
        val shipping = if (subtotal > 0) 25.0 else 0.0
        val total = subtotal + shipping

        tvSubtotal.text = "AED ${String.format("%.2f", subtotal)}"
        tvShipping.text = "AED ${String.format("%.2f", shipping)}"
        tvTotal.text = "AED ${String.format("%.2f", total)}"
    }

    private fun showSettingsDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_settings)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.findViewById<TextView>(R.id.btn_close_settings)?.setOnClickListener {
            SoundHelper.playClickSound(this)
            dialog.dismiss()
        }
        dialog.show()
    }
}