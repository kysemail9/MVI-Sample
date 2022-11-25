package com.kys.foodordersimulation.uis.intent

import com.kys.foodordersimulation.data.model.FoodOrder
import com.kys.foodordersimulation.uis.adapter.MainAdapter

sealed class DataIntent {
    object FetchData : DataIntent()
    class PlaceFoodOrder(val uniqueOrderNumber: Int, val adapter: MainAdapter) : DataIntent()
    class UpdateFoodOrderState(val foodOrder: FoodOrder, val adapterPosition: Int) :
        DataIntent()

    class DeleteFoodOrderDelivered(val foodOrder: FoodOrder, val adapterPosition: Int) :
        DataIntent()

    class LoadFoodOrderDetails(val foodOrder: FoodOrder, val adapterPosition: Int) : DataIntent()
}

