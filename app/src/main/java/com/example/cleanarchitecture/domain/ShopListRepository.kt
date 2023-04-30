package com.example.cleanarchitecture.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {

    fun editShopItem(item: ShopItem)
    fun deleteShopItem(item: ShopItem)
    fun addShopItem(item: ShopItem)
    fun getShopItem(id: Int): ShopItem
    fun getShopList(): LiveData<List<ShopItem>>

}