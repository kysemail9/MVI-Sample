package com.kys.foodordersimulation.data.api

import com.kys.foodordersimulation.data.model.Data

interface ApiHelper {

    suspend fun getData(): Data

}