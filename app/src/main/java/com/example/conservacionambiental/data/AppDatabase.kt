
package com.example.conservacionambiental.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.conservacionambiental.data.dao.AreaDao
import com.example.conservacionambiental.data.dao.MonitoreoDao
import com.example.conservacionambiental.data.dao.AmenazaDao
import com.example.conservacionambiental.data.model.Area
import com.example.conservacionambiental.data.model.Monitoreo
import com.example.conservacionambiental.data.model.Amenaza

@Database(
    entities = [
        Area::class,
        Monitoreo::class,
        Amenaza::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun areaDao(): AreaDao

    abstract fun monitoreoDao(): MonitoreoDao

    abstract fun amenazaDao(): AmenazaDao

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

        val MIGRATION_2_3 = object : Migration(2, 3) {

            override fun migrate(db: SupportSQLiteDatabase) {

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS amenazas (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        tipo TEXT NOT NULL,
                        descripcion TEXT NOT NULL,
                        nivelRiesgo TEXT NOT NULL,
                        fechaReporte TEXT NOT NULL,
                        idArea INTEGER NOT NULL,
                        FOREIGN KEY(idArea)
                            REFERENCES areas(id)
                            ON UPDATE NO ACTION
                            ON DELETE RESTRICT
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_amenazas_idArea ON amenazas(idArea)"
                )
            }
        }
    }
}