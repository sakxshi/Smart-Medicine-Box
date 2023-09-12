package com.example.smartmedicinebox.models

import android.os.Parcel
import android.os.Parcelable

data class Board(
    val medsName: String = "",
    val createdBy: String = "",
    val assignedTo: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel){
        parcel.writeString(medsName)
        parcel.writeString(createdBy)
        parcel.writeString(assignedTo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Board> {
        override fun createFromParcel(parcel: Parcel): Board {
            return Board(parcel)
        }

        override fun newArray(size: Int): Array<Board?> {
            return arrayOfNulls(size)
        }
    }
}