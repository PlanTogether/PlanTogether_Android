package com.example.plantogether.data

import android.os.Parcel
import android.os.Parcelable
import com.kakao.sdk.common.KakaoSdk.type

data class NoticeData (
    var title: String = "이벤트명 or 일정명",
    var notice: String = "알람 정보",
    var date: String = "일시"
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    constructor():this("이벤트명 or 일정명",
        "알람정보","일시")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(notice)
        parcel.writeString(date)
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