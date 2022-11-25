package com.kys.foodordersimulation.data.database

import com.kys.foodordersimulation.data.database.entities.FoodOrderTableEntity

interface BaseRoomRepository {

    fun getAllFoodOrders(): List<FoodOrderTableEntity>
    fun getFoodOrder(orderId: String): FoodOrderTableEntity
    fun deleteFoodOrder(orderId: String): Boolean
    fun saveFoodOrder(foodOrderTableEntity: FoodOrderTableEntity) : Long
    fun updateFoodOrderState(orderStateName: String, orderStateType: Int, orderId: String): Boolean
    fun updateFoodOrder(foodOrderTableEntity: FoodOrderTableEntity)
}