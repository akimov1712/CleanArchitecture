package com.example.cleanarchitecture.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cleanarchitecture.R
import com.example.cleanarchitecture.domain.ShopItem

class EditFragment: Fragment() {

    private val viewModelFactory by lazy {
        EditViewModelFactory(requireActivity().application)
    }

    private val viewModel: EditViewModel by lazy {
        ViewModelProvider(this,viewModelFactory)[EditViewModel::class.java]
    }

    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var buttonSend: Button

    private lateinit var onEditFinishedListener: OnEditFinishedListener

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    override fun onAttach(context: Context) {
        super.onAttach(context)
        /*перед закреплением фрагмента на активити вызывается onAttach, у которого context
        является тем активити, на котором он прикреплется. Проверяем если активити реализует интерфейс
        с переопределенным методом onEditFinish. Если да, то присваиваем переменной активити
        в котором переопределен метод onEditFinish. При этом на разных активити будет разная реализация
        метода onEditFinish.
         */
        if (context is OnEditFinishedListener){
            onEditFinishedListener = context
        } else{
            throw RuntimeException("Activity must implement OnEditFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParam()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        choiceMode()
        observeViewModel()
        resetErrorsTextInput()
    }

    private fun parseParam(){
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw java.lang.RuntimeException("Параметр screen mode не передан")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw java.lang.RuntimeException("Передан не допустимый mode: $mode")
        }
        screenMode = mode
        if (mode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw java.lang.RuntimeException("Открыт мод редактирования, без переданого id")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun initViews(view: View){
        etName = view.findViewById(R.id.editTextName)
        etCount = view.findViewById(R.id.editTextCount)
        buttonSend = view.findViewById(R.id.button)
    }

    private fun choiceMode(){
        when(screenMode){
            MODE_ADD -> runModeAdd()
            MODE_EDIT -> runModeEdit()
        }
    }

    private fun observeViewModel(){
        viewModel.errorInputName.observe(viewLifecycleOwner){
            val message = if (it) getString(R.string.error_input_name) else null
            etName.error = message
        }

        viewModel.errorInputCount.observe(viewLifecycleOwner){
            val message = if (it) getString(R.string.error_input_count) else null
            etCount.error = message
        }
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner){
            onEditFinishedListener.onEditFinished()
        }
    }


    private fun resetErrorsTextInput() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun runModeEdit(){
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(viewLifecycleOwner){
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        buttonSend.setOnClickListener{
            viewModel.editShopItem(etName.text.toString(),etCount.text.toString())
        }
    }

    private fun runModeAdd(){
        buttonSend.setOnClickListener{
            viewModel.addShopItem(etName.text.toString(),etCount.text.toString())
        }
    }

    interface OnEditFinishedListener{
        fun onEditFinished()
    }

    /*
    Если фрагмент должен сообщить, что-нибудь активиты, это должно быть сделано через интерфейс.
    Фрагмент не должен иметь доступ к всем методам активити(например установка макета активити
    через getActivity)
     */

    companion object {
        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newInstanceModeAdd(): EditFragment{
            return EditFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceModeEdit(shopitemId: Int): EditFragment{
            return EditFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopitemId)
                }
            }
        }
    }

}