package com.kys.foodordersimulation.data.api

import com.kys.foodordersimulation.data.model.Data

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {

    override suspend fun getData(): Data {
        return apiService.getData()
    }
}