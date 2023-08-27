package com.example.smartmedicinebox.models

import android.os.Parcel
import android.os.Parcelable

data class User (

    val id: String = "",
    val name: String = "",
    val email: String = "",
    val medicineName: String = "",
    val fcmToken: String = ""
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(name)
        writeString(email)
        writeString(medicineName)
        writeString(fcmToken)
    }



    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(source: Parcel): User {
            return User(source)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}
