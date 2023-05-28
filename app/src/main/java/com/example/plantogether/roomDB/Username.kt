package com.example.plantogether.roomDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class Username(
    @PrimaryKey var uId:Int
)
