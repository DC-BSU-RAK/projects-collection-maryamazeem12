package com.example.app2multipage

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {

    private lateinit var optionHome: ConstraintLayout
    private lateinit var optionOffice: ConstraintLayout
    private lateinit var dotHome: View
    private lateinit var dotOffice: View
    private lateinit var tvHome: TextView
    private lateinit var tvOffice: TextView
    private lateinit var payCard: View
    private lateinit var payCash: View
    private lateinit var dotCard: View
    private lateinit var dotCash: View

    private var selectedAddress: String = "Home"
    private var selectedPayment: String = "Card"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        optionHome = findViewById(R.id.option_home)
        optionOffice = findViewById(R.id.option_office)
        dotHome = findViewById(R.id.dot_home)
        dotOffice = findViewById(R.id.dot_office)
        tvHome = findViewById(R.id.tv_home)
        tvOffice = findViewById(R.id.tv_office)
        payCard = findViewById(R.id.pay_card)
        payCash = findViewById(R.id.pay_cash)
        dotCard = findViewById(R.id.dot_card)
        dotCash = findViewById(R.id.dot_cash)

        val tvSubtotal = findViewById<TextView>(R.id.tv_subtotal_val)
        val tvShipping = findViewById<TextView>(R.id.tv_shipping_val)
        val tvTotal = findViewById<TextView>(R.id.tv_total_val)
        val btnProceed = findViewById<ImageView>(R.id.btn_proceed)

        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        
        findViewById<View>(R.id.tv_checkout_title).startAnimation(fadeIn)
        findViewById<View>(R.id.iv_progress).startAnimation(fadeIn)
        findViewById<View>(R.id.option_home).startAnimation(slideUp)
        findViewById<View>(R.id.option_office).startAnimation(slideUp)

        val subtotal = CartManager.getSubtotal()
        val shipping = if (subtotal > 0) 25.0 else 0.0
        val total = subtotal + shipping

        tvSubtotal.text = String.format(Locale.US, "AED %.2f", subtotal)
        tvShipping.text = String.format(Locale.US, "AED %.2f", shipping)
        tvTotal.text = String.format(Locale.US, "AED %.2f", total)

        updateAddressSelection()
        
        val addressClickListener = View.OnClickListener {
            SoundHelper.playClickSound(this)
            selectedAddress = if (it == optionHome || it == dotHome) "Home" else "Office"
            updateAddressSelection()
            it.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in))
        }
        optionHome.setOnClickListener(addressClickListener)
        dotHome.setOnClickListener(addressClickListener)
        optionOffice.setOnClickListener(addressClickListener)
        dotOffice.setOnClickListener(addressClickListener)

        updatePaymentSelection()
        
        val paymentClickListener = View.OnClickListener {
            SoundHelper.playClickSound(this)
            selectedPayment = if (it == payCard || it == dotCard) "Card" else "Cash"
            updatePaymentSelection()
            it.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in))
        }
        payCard.setOnClickListener(paymentClickListener)
        dotCard.setOnClickListener(paymentClickListener)
        payCash.setOnClickListener(paymentClickListener)
        dotCash.setOnClickListener(paymentClickListener)

        findViewById<View>(R.id.btn_edit_home).setOnClickListener {
            SoundHelper.playClickSound(this)
            showEditAddressDialog("Home")
        }
        findViewById<View>(R.id.btn_edit_office).setOnClickListener {
            SoundHelper.playClickSound(this)
            showEditAddressDialog("Office")
        }

        findViewById<View>(R.id.btn_settings).setOnClickListener {
            SoundHelper.playClickSound(this)
            showSettingsDialog()
        }
        findViewById<View>(R.id.logo_small).setOnClickListener {
            SoundHelper.playClickSound(this)
            finish()
        }

        btnProceed.setOnClickListener {
            SoundHelper.playClickSound(this)
            val anim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
            it.startAnimation(anim)
            startActivity(Intent(this, ConfirmationActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }
    }

    private fun updateAddressSelection() {
        if (selectedAddress == "Home") {
            optionHome.isSelected = true
            optionOffice.isSelected = false
            dotHome.isSelected = true
            dotOffice.isSelected = false
            tvHome.setTextColor(Color.WHITE)
            tvOffice.setTextColor(Color.parseColor("#1D3D2F"))
        } else {
            optionHome.isSelected = false
            optionOffice.isSelected = true
            dotHome.isSelected = false
            dotOffice.isSelected = true
            tvHome.setTextColor(Color.parseColor("#1D3D2F"))
            tvOffice.setTextColor(Color.WHITE)
        }
    }

    private fun updatePaymentSelection() {
        if (selectedPayment == "Card") {
            payCard.isSelected = true
            payCash.isSelected = false
            dotCard.isSelected = true
            dotCash.isSelected = false
        } else {
            payCard.isSelected = false
            payCash.isSelected = true
            dotCard.isSelected = false
            dotCash.isSelected = true
        }
    }

    private fun showEditAddressDialog(type: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_edit_address)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        
        val etAddress = dialog.findViewById<EditText>(R.id.et_address)
        val btnSave = dialog.findViewById<View>(R.id.btn_save_address)
        
        dialog.findViewById<TextView>(R.id.tv_dialog_title).text = "EDIT $type ADDRESS"

        btnSave.setOnClickListener {
            SoundHelper.playClickSound(this)
            val newAddress = etAddress.text.toString()
            if (newAddress.isNotEmpty()) {
                Toast.makeText(this, "$type address updated to: $newAddress", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
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