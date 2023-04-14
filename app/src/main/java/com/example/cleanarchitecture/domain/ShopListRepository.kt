package com.example.cleanarchitecture.domain

interface ShopListRepository {

    fun editShopItem(item: ShopItem)
    fun deleteShopItem(item: ShopItem)
    fun insertShopItem(item: ShopItem)
    fun getShopItem(id: Int): ShopItem
    fun getShopList(): List<ShopItem>

}