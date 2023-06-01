package com.example.cleanarchitecture.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {

    suspend fun editShopItem(item: ShopItem)
    suspend fun deleteShopItem(item: ShopItem)
    suspend fun addShopItem(item: ShopItem)
    suspend fun getShopItem(id: Int): ShopItem
    fun  getShopList(): LiveData<List<ShopItem>>

}