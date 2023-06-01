package com.example.cleanarchitecture.domain

class AddShopItemUseCase(private val repository: ShopListRepository) {

    suspend fun addShopItem(item: ShopItem){
        repository.addShopItem(item)
    }
}