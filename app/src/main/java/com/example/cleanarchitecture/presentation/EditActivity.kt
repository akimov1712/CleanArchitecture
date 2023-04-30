package com.example.cleanarchitecture.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cleanarchitecture.R
import com.example.cleanarchitecture.domain.ShopItem

class EditActivity : AppCompatActivity() {

//    private lateinit var viewModel: EditViewModel
//
//    private lateinit var etName: EditText
//    private lateinit var etCount: EditText
//    private lateinit var buttonSend: Button

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        parseIntent()
//        viewModel = ViewModelProvider(this)[EditViewModel::class.java]
//        initViews()
//        AddTextChangeListeners()
        launchRightMode()
//        observeViewModel()
    }
//
//    private fun observeViewModel() {
//        viewModel.errorInputName.observe(this) {
//            val message = if (it) getString(R.string.error_input_name) else null
//            etName.error = message
//        }
//        viewModel.errorInputCount.observe(this) {
//            val message = if (it) getString(R.string.error_input_count) else null
//            etCount.error = message
//        }
//        viewModel.shouldCloseScreen.observe(this) {
//            finish()
//        }
//    }
//
    private fun launchRightMode() {
    val fragment = when (screenMode) {
            MODE_EDIT -> EditFragment.newInstanceModeEdit(shopItemId)
            MODE_ADD -> EditFragment.newInstanceModeAdd()
            else -> throw java.lang.RuntimeException("Unknown screen mode: $screenMode")
    }
    supportFragmentManager.beginTransaction().add(R.id.edit_container, fragment).commit()
}
//
//    private fun AddTextChangeListeners() {
//        etName.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                viewModel.resetErrorInputName()
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//        })
//        etCount.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                viewModel.resetErrorInputCount()
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//        })
//    }
//
//    private fun launchEditMode() {
//        viewModel.getShopItem(shopItemId)
//        viewModel.shopItem.observe(this) {
//            etName.setText(it.name)
//            etCount.setText(it.count.toString())
//        }
//        buttonSend.setOnClickListener {
//            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
//        }
//    }
//
//    private fun launchAddMode() {
//        buttonSend.setOnClickListener {
//            viewModel.insertShopItem(etName.text?.toString(), etCount.text?.toString())
//        }
//    }
//
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
//
//    private fun initViews() {
//        etName = findViewById(R.id.editTextName)
//        etCount = findViewById(R.id.editTextCount)
//        buttonSend = findViewById(R.id.button)
//    }

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