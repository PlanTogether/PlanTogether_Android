package com.example.plantogether.data

import android.os.Parcel
import android.os.Parcelable
import com.kakao.sdk.common.KakaoSdk.type

data class NoticeData (
    var key: String = "",
    var title: String = "이벤트명 or 일정명",
    var time: Long = 0L,
    var notice: String = "알람 정보"
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readString()!!
    ) {
    }

    constructor():this("고유키","이벤트명 or 일정명", 0L,
        "알람정보",)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(key)
        parcel.writeString(title)
        parcel.writeLong(time)
        parcel.writeString(notice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NoticeData> {
        override fun createFromParcel(parcel: Parcel): NoticeData {
            return NoticeData(parcel)
        }

        override fun newArray(size: Int): Array<NoticeData?> {
            return arrayOfNulls(size)
        }
    }
}