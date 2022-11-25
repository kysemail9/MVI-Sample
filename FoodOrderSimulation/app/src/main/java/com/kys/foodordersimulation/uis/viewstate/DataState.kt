package com.kys.foodordersimulation.uis.viewstate

import com.kys.foodordersimulation.data.model.Data
import com.kys.foodordersimulation.data.model.FoodOrder


sealed class DataState {

    object Inactive : DataState()
    object Loading : DataState()
    data class ResponseData(val data: Data) : DataState()
    data class Error(val error: String?) : DataState()

    // place order
    data class PlaceOrderSuccess(val orderId: String) : DataState()
    data class PlaceOrderError(val error: String?) : DataState()

    // update order state
    data class UpdateOrderStateSuccess(val orderId: String, val foodOrderStateName: String) :
        DataState()

    data class UpdateOrderStateError(val error: String?) : DataState()

    // delete delivered state
    data class DeleteOrderSuccess(val orderId: String, val adapterPosition: Int) : DataState()
    data class DeleteOrderError(val error: String?) : DataState()

    data class LoadFoodOrderDetails(val foodOrder: FoodOrder, val adapterPosition: Int) :
        DataState()
}