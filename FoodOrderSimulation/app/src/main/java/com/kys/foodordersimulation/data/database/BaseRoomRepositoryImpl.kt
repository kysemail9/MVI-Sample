package com.kys.foodordersimulation.data.database

import com.kys.foodordersimulation.data.database.entities.FoodOrderDao
import com.kys.foodordersimulation.data.database.entities.FoodOrderTableEntity

class BaseRoomRepositoryImpl(private val foodOrderDao: FoodOrderDao) : BaseRoomRepository {

    // to load list of orders
    override fun getAllFoodOrders(): List<FoodOrderTableEntity> =
        foodOrderDao.getAllFoodOrders()

    // on place order - by default create one
    override fun saveFoodOrder(foodOrderTableEntity: FoodOrderTableEntity) =
        foodOrderDao.insert(foodOrderTableEntity)


    // on - state button - click
    override fun updateFoodOrderState(
        orderStateName: String,
        orderStateType: Int,
        orderId: String
    ) =
        foodOrderDao.updateFoodOrderState(orderStateName, orderStateType, orderId) > 0

    // if want
    override fun getFoodOrder(orderId: String): FoodOrderTableEntity =
        foodOrderDao.getFoodOrder(orderId)

    // after ->  delivered && 15 seconds over
    override fun deleteFoodOrder(orderId: String) = foodOrderDao.deleteFoodOrder(orderId) > 0


    override fun updateFoodOrder(foodOrderTableEntity: FoodOrderTableEntity) =
        foodOrderDao.update(foodOrderTableEntity)
}