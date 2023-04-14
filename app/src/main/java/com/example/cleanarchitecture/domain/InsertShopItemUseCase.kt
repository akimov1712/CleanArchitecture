package com.example.cleanarchitecture.domain

class InsertShopItemUseCase(private val repository: ShopListRepository) {

    fun insertShopItem(item: ShopItem){
        repository.insertShopItem(item)
    }
}