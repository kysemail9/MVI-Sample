package com.kys.foodordersimulation.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.kys.foodordersimulation.data.repository.MainRepository
import com.kys.foodordersimulation.data.api.ApiHelper
import com.kys.foodordersimulation.data.database.BaseRoomRepository
import com.kys.foodordersimulation.uis.viewmodel.DataViewModel

class ViewModelFactory(
    private val apiHelper: ApiHelper,
    private val baseRoomRepository: BaseRoomRepository
) :
    ViewModelProvider.AndroidViewModelFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(DataViewModel::class.java)) {
            return DataViewModel(MainRepository(apiHelper, baseRoomRepository)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}