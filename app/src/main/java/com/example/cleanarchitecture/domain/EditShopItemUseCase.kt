package com.example.cleanarchitecture.domain

class EditShopItemUseCase(private val repository: ShopListRepository) {

    suspend fun editShopItem(item: ShopItem){
        repository.editShopItem(item)
    }
}