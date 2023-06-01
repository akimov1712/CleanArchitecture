package com.example.cleanarchitecture.presentation

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.cleanarchitecture.R
import com.example.cleanarchitecture.toasts.CustomToasts

class MainActivity : AppCompatActivity(), EditFragment.OnEditFinishedListener  {

    private val viewModelFactory by lazy {
        MainViewModelFactory(application)
    }
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    private lateinit var shopListAdapter: ShopListAdapter
    private var shopItemContainer: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        shopItemContainer = findViewById(R.id.fragment_container_main_activity)
        setupRecyclerView()
        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
        }
        listenerActionButton()
    }

    private fun setupRecyclerView() {
        val rvShop = findViewById<RecyclerView>(R.id.rcView)
        shopListAdapter = ShopListAdapter()
        with(rvShop) {
            adapter = shopListAdapter
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
        setupLongClickListener()
        setupClickListener()
        setupSwapListener(rvShop)
    }
    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragment_container_main_activity, fragment)
            .commit()
    }

    private fun setupSwapListener(rvShop: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShop)
    }

    private fun setupClickListener() {
        shopListAdapter.shopItemClickListener = {
            if (shopItemContainer == null){
                val intent = EditActivity.newIntentEditItem(this,it.id)
                startActivity(intent)
            } else {
                launchFragment(EditFragment.newInstanceModeEdit(it.id))
            }
        }
    }

    override fun onEditFinished() {
        CustomToasts.toastSuccess(this)
        supportFragmentManager.popBackStack()
    }

    private fun setupLongClickListener() {
        shopListAdapter.shopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }

    private fun listenerActionButton() {
        val buttonAddItem = findViewById<Button>(R.id.floatingActionButton)
        buttonAddItem.setOnClickListener {
            if (shopItemContainer == null) {
                val intent = EditActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                launchFragment(EditFragment.newInstanceModeAdd())
            }
        }
    }
}