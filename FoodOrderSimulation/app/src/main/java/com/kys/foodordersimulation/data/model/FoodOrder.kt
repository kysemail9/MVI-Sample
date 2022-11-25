package com.kys.foodordersimulation.data.model

data class FoodOrder(
    var orderId: String,
    val orderDetails: String?,
    var orderStateNameNow: String,
    var orderStateTypeNow: Int = 0, // 1 - New , 2 - Preparing, 3 - Ready , 4 - Delivered
    val orderCost: Long = 0
)