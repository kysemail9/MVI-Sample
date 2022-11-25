package com.kys.foodordersimulation.uis.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.kys.foodordersimulation.R
import com.kys.foodordersimulation.data.api.ApiHelperImpl
import com.kys.foodordersimulation.data.api.RetrofitBuilder
import com.kys.foodordersimulation.data.database.BaseRoomRepositoryImpl
import com.kys.foodordersimulation.data.database.DatabaseRepository
import com.kys.foodordersimulation.data.model.FoodOrder
import com.kys.foodordersimulation.uis.adapter.MainAdapter
import com.kys.foodordersimulation.uis.intent.DataIntent
import com.kys.foodordersimulation.uis.viewmodel.DataViewModel
import com.kys.foodordersimulation.uis.viewstate.DataState
import com.kys.foodordersimulation.util.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), MainAdapter.FoodOrderItemChangeStateCallbacks {

    private lateinit var dataViewModel: DataViewModel
    private var adapter = MainAdapter(arrayListOf(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUI()
        setupViewModel()
        observeViewModel()
        setupClicks()
        setInitialData()
    }

    private fun setInitialData() {
        lifecycleScope.launch {
            dataViewModel.dataIntent.value = DataIntent.FetchData
        }
    }

    private fun setupClicks() {
        buttonPlaceOrder.setOnClickListener {
            Log.e("MA", "button place order clicked")
            lifecycleScope.launch { //dataViewModel.dataIntent.send(DataIntent.FetchData)
                try {
                    val orderNumber = editOrderNumber.text.trim().toString().toInt()
                    dataViewModel.dataIntent.value = DataIntent.PlaceFoodOrder(orderNumber, adapter)

                } catch (e: Exception) {
                    Toast.makeText(
                        this@MainActivity,
                        "Please enter only unique number",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.run {
            addItemDecoration(
                DividerItemDecoration(
                    recyclerView.context,
                    (recyclerView.layoutManager as LinearLayoutManager).orientation
                )
            )
        }
        recyclerView.adapter = adapter
    }


    private fun setupViewModel() {
        val db = Room.databaseBuilder(
            applicationContext,
            DatabaseRepository::class.java, "food-orders-db"
        ).allowMainThreadQueries()
            .build()

        dataViewModel = ViewModelProvider(
            this, ViewModelFactory(
                ApiHelperImpl(
                    RetrofitBuilder.apiService
                ),
                BaseRoomRepositoryImpl(
                    db.foodOrderDao
                )
            )
        )[DataViewModel::class.java]
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            dataViewModel.dataState.collect {
                when (it) {
                    is DataState.Inactive -> {
                        Log.d("Inactive", "Inactive State")
                    }
                    is DataState.Loading -> {
                        buttonPlaceOrder.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                    }
                    is DataState.ResponseData -> {
                        Log.e("MA", "K ResponseData SUCCESS")
                        progressBar.visibility = View.GONE
                        buttonPlaceOrder.visibility = View.VISIBLE
                        renderList(it.data.data)
                    }
                    is DataState.Error -> {
                        progressBar.visibility = View.GONE
                        buttonPlaceOrder.visibility = View.VISIBLE
                        Toast.makeText(this@MainActivity, it.error, Toast.LENGTH_LONG).show()
                    }
                    is DataState.UpdateOrderStateSuccess -> {
                        Log.e("MA", "K update order state SUCCESS")
                        Toast.makeText(
                            this@MainActivity,
                            "Order id : ${it.orderId} is ${it.foodOrderStateName}",
                            Toast.LENGTH_LONG
                        ).show()
                        refreshList()
                    }
                    is DataState.UpdateOrderStateError -> {
                        Log.e("MA", "K update order state ERROR = ${it.error}")
                        Toast.makeText(this@MainActivity, it.error, Toast.LENGTH_LONG).show()
                    }
                    is DataState.DeleteOrderSuccess -> {
                        Log.e("MA", "K delete order state SUCCESS ")
                        Toast.makeText(
                            this@MainActivity,
                            "Order id : ${it.orderId} is Deleted",
                            Toast.LENGTH_LONG
                        ).show()
                        deleteAndRefreshList(it.adapterPosition)
                    }
                    is DataState.DeleteOrderError -> {
                        Log.e("MA", "K delete order state ERROR = ${it.error}")
                        Toast.makeText(this@MainActivity, it.error, Toast.LENGTH_LONG).show()
                    }
                    is DataState.PlaceOrderSuccess -> {
                        Log.e("MA", "K place order SUCCESS")
                        progressBar.visibility = View.GONE
                        buttonPlaceOrder.visibility = View.VISIBLE

                        addAndRefreshList()
                    }
                    is DataState.PlaceOrderError -> {
                        progressBar.visibility = View.GONE
                        buttonPlaceOrder.visibility = View.VISIBLE
                        Log.e("MA", "K place order ERROR = ${it.error}")
                    }
                    is DataState.LoadFoodOrderDetails -> {

                        Log.e("MA", "K load food order details")

                        // load new activity or fragment

                    }
                }
            }
        }
    }

    private fun addAndRefreshList() {
        //DataIntent.FetchData
    }

    private fun refreshList() {
        adapter.notifyDataSetChanged()
    }

    private fun deleteAndRefreshList(adapterPosition: Int) {
        adapter.removeItem(adapterPosition)
        adapter.notifyDataSetChanged()
    }

    private fun renderList(foodOrders: List<FoodOrder>) {
        recyclerView.visibility = View.VISIBLE
        foodOrders.let { listOfUsers -> listOfUsers.let { adapter.addData(it) } }
        adapter.notifyDataSetChanged()
    }

    override fun onFoodOrderItemStateClicked(foodOrder: FoodOrder, adapterPosition: Int) {
        Log.e("MA", "K item clicked at index $adapterPosition")
        lifecycleScope.launch {
            /*dataViewModel.dataIntent.send(
                DataIntent.UpdateFoodOrderState(
                    foodOrder,
                    adapterPosition
                )
            )*/
            dataViewModel.dataIntent.value =
                DataIntent.UpdateFoodOrderState(foodOrder, adapterPosition)
        }
    }

    override fun onFoodOrderItemClicked(foodOrder: FoodOrder, adapterPosition: Int) {
        Log.e("MA", "K food order clicked ")
        lifecycleScope.launch {
            dataViewModel.dataIntent.value =
                DataIntent.LoadFoodOrderDetails(foodOrder, adapterPosition)
        }
    }
}
