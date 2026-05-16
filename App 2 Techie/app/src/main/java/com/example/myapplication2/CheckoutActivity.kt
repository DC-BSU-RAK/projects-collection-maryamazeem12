package com.example.myapplication2

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
        val tvTitle = findViewById<TextView>(R.id.tv_checkout_title)
        val ivProgress = findViewById<ImageView>(R.id.iv_progress)
        val logoSmall = findViewById<ImageView>(R.id.logo_small)
        val btnSettings = findViewById<ImageView>(R.id.btn_settings)

        // Advanced Entry animations
        val entrance = AnimationUtils.loadAnimation(this, R.anim.entrance_anim)
        val overshootSlide = AnimationUtils.loadAnimation(this, R.anim.overshoot_slide_in)
        val smoothScale = AnimationUtils.loadAnimation(this, R.anim.smooth_scale_in)
        val shake = AnimationUtils.loadAnimation(this, R.anim.shake)
        val rotatePopIn = AnimationUtils.loadAnimation(this, R.anim.rotate_pop_in)
        
        tvTitle?.startAnimation(entrance)
        ivProgress?.startAnimation(entrance)
        optionHome.startAnimation(overshootSlide)
        optionOffice.startAnimation(overshootSlide)
        findViewById<View>(R.id.payment_section_title)?.startAnimation(entrance)
        payCard.startAnimation(smoothScale)
        payCash.startAnimation(smoothScale)
        findViewById<View>(R.id.summary_box)?.startAnimation(overshootSlide)
        logoSmall?.startAnimation(rotatePopIn)
        btnSettings?.startAnimation(rotatePopIn)
        btnProceed.startAnimation(rotatePopIn)
        
        // Add shaky heading after entrance
        tvTitle?.postDelayed({ tvTitle.startAnimation(shake) }, 1200)

        val subtotal = CartManager.getSubtotal()
        val shipping = if (subtotal > 0) 25.0 else 0.0
        val total = subtotal + shipping

        tvSubtotal.text = String.format(Locale.US, "AED %.2f", subtotal)
        tvShipping.text = String.format(Locale.US, "AED %.2f", shipping)
        tvTotal.text = String.format(Locale.US, "AED %.2f", total)

        updateAddressSelection()
        
        val addressClickListener = View.OnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            selectedAddress = if (it == optionHome || it == dotHome) "Home" else "Office"
            updateAddressSelection()
        }
        optionHome.setOnClickListener(addressClickListener)
        dotHome.setOnClickListener(addressClickListener)
        optionOffice.setOnClickListener(addressClickListener)
        dotOffice.setOnClickListener(addressClickListener)

        updatePaymentSelection()
        
        val paymentClickListener = View.OnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            selectedPayment = if (it == payCard || it == dotCard) "Card" else "Cash"
            updatePaymentSelection()
        }
        payCard.setOnClickListener(paymentClickListener)
        dotCard.setOnClickListener(paymentClickListener)
        payCash.setOnClickListener(paymentClickListener)
        dotCash.setOnClickListener(paymentClickListener)

        findViewById<View>(R.id.btn_edit_home).setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            showEditAddressDialog("Home")
        }
        findViewById<View>(R.id.btn_edit_office).setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            showEditAddressDialog("Office")
        }

        btnSettings.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            showSettingsDialog()
        }
        logoSmall.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            finish()
        }

        btnProceed.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            it.postDelayed({
                startActivity(Intent(this, ConfirmationActivity::class.java))
                overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit)
                finish()
            }, 200)
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
        // Feedback animation
        val pop = AnimationUtils.loadAnimation(this, R.anim.pop_in)
        if (selectedAddress == "Home") dotHome.startAnimation(pop) else dotOffice.startAnimation(pop)
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
        // Feedback animation
        val pop = AnimationUtils.loadAnimation(this, R.anim.pop_in)
        if (selectedPayment == "Card") dotCard.startAnimation(pop) else dotCash.startAnimation(pop)
    }

    private fun showEditAddressDialog(type: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_edit_address)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        
        val dialogView = dialog.window?.decorView
        dialogView?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_pop_in))

        val etAddress = dialog.findViewById<EditText>(R.id.et_address)
        val btnSave = dialog.findViewById<View>(R.id.btn_save_address)
        
        dialog.findViewById<TextView>(R.id.tv_dialog_title).text = "EDIT $type ADDRESS"

        btnSave.setOnClickListener {
            SoundHelper.playClickSound(this)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            val newAddress = etAddress.text.toString()
            if (newAddress.isNotEmpty()) {
                Toast.makeText(this, "$type address updated to: $newAddress", Toast.LENGTH_SHORT).show()
                it.postDelayed({ dialog.dismiss() }, 200)
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
        
        val dialogView = dialog.window?.decorView
        dialogView?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_pop_in))

        dialog.findViewById<TextView>(R.id.btn_close_settings)?.setOnClickListener {
            SoundHelper.playClickSound(this)
            dialog.dismiss()
        }
        dialog.show()
    }
}