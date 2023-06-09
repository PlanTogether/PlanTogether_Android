package com.example.plantogether.data

import android.os.Parcel
import android.os.Parcelable

data class EventData(
    var id: String = "고유키",
    var type: Int = 1,
    var title: String? = "이벤트명 or 일정명",
    var place: String = "장소",
    var date: String = "일시",
    var time: String = "시간",
    var detail: String = "세부정보",
    var participantName: MutableList<String> = mutableListOf<String>()
// 1이면 이벤트 아이템, 2이면 일정 아이템이 recyclerView에 추가되는 방식

) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList() ?: mutableListOf<String>()
    ) {
    }

    constructor():this("고유키", 1, "이벤트명 or 일정명",
        "장소","날짜", "시간", "세부정보")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeInt(type)
        parcel.writeString(title)
        parcel.writeString(place)
        parcel.writeString(date)
        parcel.writeString(time)
        parcel.writeString(detail)
        parcel.writeStringList(participantName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EventData> {
        override fun createFromParcel(parcel: Parcel): EventData {
            return EventData(parcel)
        }

        override fun newArray(size: Int): Array<EventData?> {
            return arrayOfNulls(size)
        }
    }
}
