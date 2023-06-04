package com.example.plantogether.dialog.data

import android.os.Parcel
import android.os.Parcelable

data class EventData(
   var title: String = "이벤트명",
   var place: String = "장소",
   var date: String = "일시",
   var detailInfo: String = "추가정보"

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "이벤트명",
        parcel.readString() ?: "장소",
        parcel.readString() ?: "일시",
        parcel.readString() ?: "추가정보"
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(place)
        parcel.writeString(date)
        parcel.writeString(detailInfo)
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
