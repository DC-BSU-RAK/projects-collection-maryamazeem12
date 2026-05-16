package com.example.app2multipage

object CartManager {
    data class CartItem(
        val name: String,
        val variant: String,
        val price: Double,
        val imageResId: Int,
        var quantity: Int = 1
    )

    private val cartList = mutableListOf<CartItem>()

    fun addToCart(item: CartItem) {
        val existing = cartList.find { it.name == item.name && it.variant == item.variant }
        if (existing != null) {
            existing.quantity++
        } else {
            cartList.add(item)
        }
    }

    fun getCartItems(): MutableList<CartItem> = cartList

    fun removeItem(item: CartItem) {
        cartList.remove(item)
    }

    fun clearCart() {
        cartList.clear()
    }

    fun getSubtotal(): Double = cartList.sumOf { it.price * it.quantity }
}