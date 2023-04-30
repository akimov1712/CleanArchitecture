package com.example.cleanarchitecture.domain

class AddShopItemUseCase(private val repository: ShopListRepository) {

    fun addShopItem(item: ShopItem){
        repository.addShopItem(item)
    }
}