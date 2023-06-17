package com.example.plantogether.roomDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey var username:String
) {
    constructor():this("사용자명")
}
