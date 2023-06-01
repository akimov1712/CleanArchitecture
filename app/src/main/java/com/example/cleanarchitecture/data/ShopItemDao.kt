package com.example.cleanarchitecture.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShopItemDao {

    @Query("SELECT * FROM shopItem")
    fun getShopList(): LiveData<List<ShopItemDbModel>>

    @Query("SELECT * FROM shopItem WHERE id=:shopItemId")
    suspend fun getShopItem(shopItemId: Int): ShopItemDbModel

    @Query("DELETE FROM shopItem WHERE id=:shopItemId")
    suspend fun deleteShopItem(shopItemId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addShopItem(shopItem: ShopItemDbModel)

}