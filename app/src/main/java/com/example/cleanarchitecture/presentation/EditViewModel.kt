package com.example.cleanarchitecture.presentation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cleanarchitecture.data.ShopListRepositoryImpl
import com.example.cleanarchitecture.domain.AddShopItemUseCase
import com.example.cleanarchitecture.domain.EditShopItemUseCase
import com.example.cleanarchitecture.domain.GetShopItemUseCase
import com.example.cleanarchitecture.domain.ShopItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class EditViewModel(application: Application) : ViewModel() {

    private val repository = ShopListRepositoryImpl(application)

    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val getShopItemUseCase = GetShopItemUseCase(repository)

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shopitem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopitem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen


    fun getShopItem(shopItemId: Int) {
        scope.launch {
            val item = getShopItemUseCase.getShopItem(shopItemId)
            _shopitem.value = item
        }
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            scope.launch {
                val shopItem = ShopItem(name, count, true)
                addShopItemUseCase.addShopItem(shopItem)
                finishWork()
            }
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            _shopitem.value?.let {
                scope.launch {
                    val item = it.copy(name = name, count = count)
                    editShopItemUseCase.editShopItem(item)
                    finishWork()
                }
            }
        }
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (ex: Exception) {
            0
        }
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}