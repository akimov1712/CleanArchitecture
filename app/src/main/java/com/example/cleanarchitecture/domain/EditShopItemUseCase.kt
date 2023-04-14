package com.example.cleanarchitecture.domain

class EditShopItemUseCase(private val repository: ShopListRepository) {

    fun editShopItem(item: ShopItem){
        repository.editShopItem(item)
    }
}