package com.example.plantogether.roomDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "place") var place: String,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "detail") var detail: String
    )

