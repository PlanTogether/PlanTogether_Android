package com.example.plantogether.roomDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plans")
data class Plan(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "type") var type: Int,
    @ColumnInfo(name = "title") var title: String, // 일정명
    @ColumnInfo(name = "date") var date: String, // 날짜
    @ColumnInfo(name = "time") var time: String // 시간

) {
    constructor():this(0, 1, "일정명", "날짜", "시간")
}
