package com.example.gp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gp.list.Disease

@Database(entities = [Disease::class], version = 3, exportSchema = false)
abstract class DiseaseDatabase: RoomDatabase() {
    abstract fun userDao(): DiseaseDao

    companion object {
        private var INSTANCE: DiseaseDatabase? = null

        fun getDatabase(context: Context): DiseaseDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DiseaseDatabase::class.java,
                    "disease_database"
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance
                return instance
            }
        }
    }
}