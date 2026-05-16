package com.example.myapplication

object FavoriteManager {
    data class Product(
        val name: String,
        val variant: String,
        val price: String,
        val rating: Float,
        val reviews: String,
        val imageResId: Int
    )

    private val favoriteList = mutableListOf<Product>()

    fun toggleFavorite(product: Product): Boolean {
        val existing = favoriteList.find { it.name == product.name }
        return if (existing != null) {
            favoriteList.remove(existing)
            false
        } else {
            favoriteList.add(product)
            true
        }
    }

    fun isFavorite(productName: String): Boolean {
        return favoriteList.any { it.name == productName }
    }

    fun getFavorites(): List<Product> = favoriteList

    fun removeFavorite(product: Product) {
        favoriteList.removeAll { it.name == product.name }
    }
}