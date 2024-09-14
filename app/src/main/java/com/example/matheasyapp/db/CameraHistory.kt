package com.example.matheasyapp.db

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "camera_history")

data class CameraHistory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val result: String,
    val solution: String,
    val base64: String,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(result)
        parcel.writeString(solution)
        parcel.writeString(base64)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CameraHistory> {
        override fun createFromParcel(parcel: Parcel): CameraHistory {
            return CameraHistory(parcel)
        }

        override fun newArray(size: Int): Array<CameraHistory?> {
            return arrayOfNulls(size)
        }
    }
}

