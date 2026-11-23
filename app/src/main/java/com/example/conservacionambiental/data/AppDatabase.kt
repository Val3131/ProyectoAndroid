
package com.example.conservacionambiental.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.conservacionambiental.data.dao.AreaDao
import com.example.conservacionambiental.data.dao.MonitoreoDao
import com.example.conservacionambiental.data.model.Area
import com.example.conservacionambiental.data.model.Monitoreo

@Database(
    entities = [Area::class, Monitoreo::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun areaDao(): AreaDao

    abstract fun monitoreoDao(): MonitoreoDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS monitoreos (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        areaId INTEGER NOT NULL,
                        fecha TEXT NOT NULL,
                        tipo TEXT NOT NULL,
                        resultado TEXT NOT NULL,
                        FOREIGN KEY(areaId)
                            REFERENCES areas(id)
                            ON UPDATE NO ACTION
                            ON DELETE RESTRICT
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_monitoreos_areaId ON monitoreos(areaId)"
                )
            }
        }
    }
}
