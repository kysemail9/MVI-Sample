package com.kys.foodordersimulation.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kys.foodordersimulation.data.database.entities.FoodOrderDao
import com.kys.foodordersimulation.data.database.entities.FoodOrderTableEntity

@Database(entities = [FoodOrderTableEntity::class], version = 1)
abstract class DatabaseRepository : RoomDatabase() {
    abstract val foodOrderDao: FoodOrderDao
}