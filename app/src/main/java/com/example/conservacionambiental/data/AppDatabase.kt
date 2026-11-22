package com.example.conservacionambiental.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.conservacionambiental.data.dao.AreaDao
import com.example.conservacionambiental.data.model.Area

@Database(
    entities = [Area::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun areaDao(): AreaDao
}
