package com.kys.foodordersimulation.data.database

data class FoodOrderData(
    val orderId: String,
    val orderDetails: String?,
    val orderStateNameNow: String,
    val orderCost: Long = 0,
    val orderStateTypeNow: Int = 0 // 1 - New , 2 - Preparing, 3 - Ready , 4 - Delivered
)