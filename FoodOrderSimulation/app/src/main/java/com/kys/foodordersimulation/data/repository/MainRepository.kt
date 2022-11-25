package com.kys.foodordersimulation.data.repository

import com.kys.foodordersimulation.data.api.ApiHelper
import com.kys.foodordersimulation.data.database.BaseRoomRepository
import com.kys.foodordersimulation.data.database.entities.FoodOrderTableEntity
import com.kys.foodordersimulation.data.model.FoodOrder


class MainRepository(
    private val apiHelper: ApiHelper,
    private val baseRoomRepository: BaseRoomRepository
) {

    suspend fun getApiData() = apiHelper.getData()

    fun getFoodOrders() = baseRoomRepository.getAllFoodOrders()

    fun updateFoodOrderState(foodOrder: FoodOrder) =
        baseRoomRepository.updateFoodOrderState(
            foodOrder.orderStateNameNow,
            foodOrder.orderStateTypeNow, foodOrder.orderId
        )


    fun deleteDeliveredFoodOrder(foodOrder: FoodOrder) =
        baseRoomRepository.deleteFoodOrder(foodOrder.orderId)

    fun placeDummyOrder(newFoodOrderId: String) = baseRoomRepository.saveFoodOrder(
        FoodOrderTableEntity(
            newFoodOrderId, // it must be unique
            "order $newFoodOrderId",
            "New",
            1,
            12
        )
    )

}