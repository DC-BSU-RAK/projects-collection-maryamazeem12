package com.example.myapplication

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
        val existing = cartList.find { it.name == item.name }
        if (existing != null) {
            existing.quantity++
        } else {
            cartList.add(item)
        }
    }

    fun removeFromCart(productName: String) {
        val existing = cartList.find { it.name == productName }
        if (existing != null) {
            if (existing.quantity > 1) {
                existing.quantity--
            } else {
                cartList.remove(existing)
            }
        }
    }

    fun getQuantity(productName: String): Int {
        return cartList.find { it.name == productName }?.quantity ?: 0
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