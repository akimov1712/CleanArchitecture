package com.example.cleanarchitecture.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("shopItem")
data class ShopItemDbModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val count: Int,
    val enabled: Boolean

)