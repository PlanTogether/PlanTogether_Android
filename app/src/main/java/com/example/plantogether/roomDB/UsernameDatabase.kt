package com.example.plantogether.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.plantogether.roomDB.Username
import com.example.plantogether.roomDB.UsernameDAO
@Database(
    entities = [Username::class],
    version =1
)
abstract class UsernameDatabase:RoomDatabase(){
    abstract fun usernameDao(): UsernameDAO

    companion object {
        private  var INSTANCE: UsernameDatabase? = null

        fun getDatabase(context: Context): UsernameDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }

            val instance = Room.databaseBuilder(
                context,
                UsernameDatabase::class.java,
                "usernamedb"
            ).build()

            INSTANCE = instance
            return instance
        }
    }
}