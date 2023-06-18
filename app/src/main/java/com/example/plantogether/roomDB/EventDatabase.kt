package com.example.plantogether.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Event::class, Plan::class, User::class, Notice::class],
    version = 2
)
abstract class EventDatabase:RoomDatabase(){
    abstract fun eventDao(): EventDAO




    companion object {
        private  var INSTANCE: EventDatabase? = null


        fun getDatabase(context: Context): EventDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }

            val instance = Room.databaseBuilder(
                context,
                EventDatabase::class.java,
                "eventdb"
            ).build()

            INSTANCE = instance
            return instance
        }
    }
}