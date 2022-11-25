package com.kys.foodordersimulation.uis.viewmodel

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kys.foodordersimulation.data.model.Data
import com.kys.foodordersimulation.data.model.FoodOrder
import com.kys.foodordersimulation.data.repository.MainRepository
import com.kys.foodordersimulation.uis.adapter.MainAdapter
import com.kys.foodordersimulation.uis.intent.DataIntent
import com.kys.foodordersimulation.uis.viewstate.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DataViewModel(
    private val repository: MainRepository
) : ViewModel() {

    val dataIntent = MutableStateFlow<DataIntent?>(null)
    val dataState = MutableStateFlow<DataState>(DataState.Inactive)

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            dataIntent.collect {
                when (it) {
                    is DataIntent.FetchData -> {
                        fetchData()
                    }
                    is DataIntent.PlaceFoodOrder -> {
                        placeFoodOrder(it.uniqueOrderNumber, it.adapter)
                    }
                    is DataIntent.UpdateFoodOrderState -> {
                        Log.e("DVM", "K update food order is intent : ${it.adapterPosition}")
                        updateFoodOrderStateForOrder(it.foodOrder, it.adapterPosition)
                    }
                    is DataIntent.DeleteFoodOrderDelivered -> { // from where is it called
                        Log.e("DVM", "K delete delivered is intent : ${it.adapterPosition}")
                        updateFoodOrderStateForOrder(it.foodOrder, it.adapterPosition)
                    }
                    is DataIntent.LoadFoodOrderDetails -> {
                        Log.e("DVM", "K load food order details ")
                        loadFoodOrderDetails(it.foodOrder, it.adapterPosition)
                    }
                }

            }
        }
    }

    private fun loadFoodOrderDetails(foodOrder: FoodOrder, adapterPosition: Int) {
        Log.e("DVM", "K loadFoodOrderDetails")

        viewModelScope.launch {
            dataState.value = DataState.Loading // can be different loading
            dataState.value = try {

                DataState.LoadFoodOrderDetails(foodOrder, adapterPosition)

            } catch (e: Exception) {
                DataState.Error(e.localizedMessage)
            }
        }
    }

    private fun placeFoodOrder(uniqueOrderNumber: Int, adapter: MainAdapter) {
        viewModelScope.launch {
            dataState.value = DataState.Loading // can be different loading
            dataState.value = try {

                Log.e("DVM", "K place order success & newFoodOrderId will be : $uniqueOrderNumber")

                // 1- update list
                var newList = adapter.getList()
                Log.e("DVM", "K new list size before = " + newList.size)
                newList.add(
                    FoodOrder(
                        uniqueOrderNumber.toString(), // it must be unique
                        "order $uniqueOrderNumber",
                        "New",
                        1,
                        12
                    )
                )
                adapter.updateData(newList)
                Log.e("DVM", "K new list size after = " + newList.size)

                // 2- update db
                var res = repository.placeDummyOrder(uniqueOrderNumber.toString())
                Log.e("DVM", "K place dummy order returned  = $res")
                if (res > 0) {
                    DataState.PlaceOrderSuccess(uniqueOrderNumber.toString())
                } else {
                    DataState.PlaceOrderError("Insert new entry failed")
                }
            } catch (e: Exception) {
                DataState.Error(e.localizedMessage)
            }
        }
    }

    private fun fetchData() {
        Log.e("DVM", "K fetch Data")
        viewModelScope.launch {
            dataState.value = DataState.Loading
            dataState.value = try {
                Log.e("DVM", "K fetch Data -> try")
                // data from api  - DataState.ResponseData(repository.getApiData())
                // data from db
                val currentFoodOrders = ArrayList<FoodOrder>()
                val dbData = repository.getFoodOrders()
                for (i in dbData.indices) {
                    currentFoodOrders.add(
                        FoodOrder(
                            dbData[i].orderId, dbData[i].orderDetails,
                            dbData[i].orderStateNameNow, dbData[i].orderStateTypeNow,
                            dbData[i].orderCost
                        )
                    )
                }
                DataState.ResponseData(Data(currentFoodOrders, 0))
            } catch (e: Exception) {
                DataState.Error(e.localizedMessage)
            }
        }
    }

    private fun updateFoodOrderStateForOrder(foodOrder: FoodOrder, adapterPosition: Int) {
        when (foodOrder.orderStateTypeNow) {
            1 -> { // change to 2
                updateOrder("Preparing", 2, foodOrder)
            }
            2 -> { // change to 3
                updateOrder("Ready", 3, foodOrder)
            }
            3 -> { // change to 4
                updateOrder("Delivered", 4, foodOrder)
            }
            4 -> { // delete after 15 seconds // this should be automatic job
                Log.e("DVM", "K 4 - foodOrderStateType = " + foodOrder.orderStateTypeNow)
                deleteAfterSomeTime(foodOrder, adapterPosition)
            }
        }
    }

    private fun deleteAfterSomeTime(foodOrder: FoodOrder, adapterPosition: Int) { // timer 15 sec
        // this timer may leak the data like this
        val timer = object : CountDownTimer(15000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                Log.e("DVM", "K timer finished")
                viewModelScope.launch {
                    //dataState.value = DataState.Loading // can show small progress on button
                    dataState.value = try {
                        if (repository.deleteDeliveredFoodOrder(foodOrder)) {
                            Log.e("DVM", "K delete success")
                            DataState.DeleteOrderSuccess(foodOrder.orderId, adapterPosition)
                        } else {
                            Log.e("DVM", "K delete failed")
                            DataState.DeleteOrderError(foodOrder.orderId)
                        }
                    } catch (e: Exception) {
                        DataState.Error(e.localizedMessage)
                    }
                }
            }
        }
        timer.start()
    }

    private fun updateOrder(
        orderStateNameNew: String,
        orderStateTypeNew: Int,
        foodOrder: FoodOrder,
    ) {
        Log.e(
            "DVM",
            "K orderStateNameNew = $orderStateNameNew & orderStateTypeNew = $orderStateTypeNew"
        )
        viewModelScope.launch {
            //dataState.value = DataState.Loading // can show small progress on button
            dataState.value = try {
                foodOrder.orderStateNameNow = orderStateNameNew
                foodOrder.orderStateTypeNow = orderStateTypeNew
                if (repository.updateFoodOrderState(foodOrder)) {
                    Log.e("DVM", "K update food order STATE success")
                    DataState.UpdateOrderStateSuccess(
                        foodOrder.orderId,
                        foodOrder.orderStateNameNow
                    )
                } else {
                    Log.e("DVM", "K update food order STATE error")
                    DataState.UpdateOrderStateError(foodOrder.orderId)
                }
            } catch (e: Exception) {
                DataState.Error(e.localizedMessage)
            }
        }
    }
}