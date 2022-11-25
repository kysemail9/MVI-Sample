package com.kys.foodordersimulation.data.api

import com.kys.foodordersimulation.data.model.Data
import retrofit2.http.GET

interface ApiService {

   @GET("orders")
   suspend fun getData(): Data
}