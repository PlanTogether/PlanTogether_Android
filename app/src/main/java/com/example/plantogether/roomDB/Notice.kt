package com.example.plantogether.roomDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



//type 0 : plan새로 생성, 1 : event 새로 생성
//2: 이벤트 초대장 도착 내용, 3 : 초대 수락 or 거절, 4 : 플랜 변경 내역 5 : 이벤트 변경 내역
@Entity(tableName = "notices")
data class Notice (
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "pid") var pid : Int,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "time") var time: String,
    @ColumnInfo(name = "type") var type : Int
)