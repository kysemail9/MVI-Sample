package com.kys.foodordersimulation.data.database.entities

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kys.foodordersimulation.data.database.FoodOrderData

@Entity
open class FoodOrderTableEntity(
    @PrimaryKey var orderId: String = "",
    val orderDetails: String? = null,
    val orderStateNameNow: String = "New",
    val orderStateTypeNow: Int = 1, // 1 - New , 2 - Preparing, 3 - Ready , 4 - Delivered
    val orderCost: Long = 0
) {
    companion object {
        fun from(
            foodOrder: FoodOrderData
        ): FoodOrderTableEntity {

            return FoodOrderTableEntity(
                orderId = foodOrder.orderId ?: "",
                orderDetails = foodOrder.orderDetails,
                orderStateNameNow = foodOrder.orderStateNameNow,
                orderStateTypeNow = foodOrder.orderStateTypeNow,
                orderCost = foodOrder.orderCost
            )
        }
    }
}

@Dao
interface FoodOrderDao {

    @Query("SELECT * FROM FoodOrderTableEntity")
    fun getAllFoodOrders(): List<FoodOrderTableEntity>

    @Query("SELECT * FROM FoodOrderTableEntity WHERE orderId =:orderId")
    fun getFoodOrder(orderId: String): FoodOrderTableEntity

    @Query("SELECT * FROM FoodOrderTableEntity")
    fun getAll(): LiveData<List<FoodOrderTableEntity>>

    @Update
    fun update(foodOrderTable: FoodOrderTableEntity)

    @Insert
    fun insert(foodOrderTable: FoodOrderTableEntity) : Long

    @Query("UPDATE FoodOrderTableEntity SET  orderStateNameNow =:orderStateName, orderStateTypeNow =:orderStateType WHERE orderId =:orderId")
    fun updateFoodOrderState(orderStateName: String, orderStateType: Int, orderId: String): Int

    @Query("DELETE FROM FoodOrderTableEntity WHERE orderId =:orderId")
    fun deleteFoodOrder(orderId: String): Int
}