package com.example.cleanarchitecture.data

import com.example.cleanarchitecture.domain.ShopItem
import com.example.cleanarchitecture.domain.ShopListRepository

object ShopListRepositoryImpl: ShopListRepository {

    private val shopList = mutableListOf<ShopItem>()

    private var autoIncrementId = 0

    override fun insertShopItem(item: ShopItem) {
        if (item.id == ShopItem.UNDEFINED_ID){
            item.id = autoIncrementId++
        }
        shopList.add(item)
    }

    override fun editShopItem(item: ShopItem) {
        val oldShopItem = getShopItem(item.id)
        deleteShopItem(oldShopItem)
        insertShopItem(item)
    }

    override fun deleteShopItem(item: ShopItem) {
        shopList.remove(item)
    }

    override fun getShopItem(id: Int): ShopItem {
        return shopList.find { it.id == id } ?: throw java.lang.RuntimeException("Элемент с $id не найден")
    }

    override fun getShopList(): List<ShopItem> {
        return shopList.toList() // to List возвращает копию ориг. коллекции
    }
}