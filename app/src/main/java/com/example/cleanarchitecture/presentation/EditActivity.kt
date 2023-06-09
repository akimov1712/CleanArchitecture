package com.example.cleanarchitecture.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cleanarchitecture.R
import com.example.cleanarchitecture.domain.ShopItem
import com.example.cleanarchitecture.toasts.CustomToasts

class EditActivity : AppCompatActivity(), EditFragment.OnEditFinishedListener{
    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        parseIntent()
        if (savedInstanceState == null){
            launchRightMode()
        }
    }
    private fun parseIntent() {
        if (!intent.hasExtra(SCREEN_MODE)) {
            throw java.lang.RuntimeException("Параметр screen mode не передан")
        }
        val mode = intent.getStringExtra(SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw java.lang.RuntimeException("Передан не допустимый mode: $mode")
        }
        screenMode = mode
        if (mode == MODE_EDIT) {
            if (!intent.hasExtra(SHOP_ITEM_ID)) {
                throw java.lang.RuntimeException("Открыт мод редактирования, без переданого id")
            }
            shopItemId = intent.getIntExtra(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun launchRightMode() {
    val fragment = when (screenMode) {
            MODE_EDIT -> EditFragment.newInstanceModeEdit(shopItemId)
            MODE_ADD -> EditFragment.newInstanceModeAdd()
            else -> throw java.lang.RuntimeException("Unknown screen mode: $screenMode")
    }
    supportFragmentManager.beginTransaction()
        .replace(R.id.edit_container, fragment)
        .commit()
    }

    override fun onEditFinished() {
        finish()
        CustomToasts.toastSuccess(this)
    }

    companion object {
        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, EditActivity::class.java)
            intent.putExtra(SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, shopitemId: Int): Intent {
            val intent = Intent(context, EditActivity::class.java)
            intent.putExtra(SCREEN_MODE, MODE_EDIT)
            intent.putExtra(SHOP_ITEM_ID, shopitemId)
            return intent
        }
    }
}