package com.example.matheasyapp.model

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "pdf_start")

class PdfFile(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var path: String,
    var name: String,
    var size: String,
    var time: String,
    var piority: Boolean = false,
    var timeEdit : String
) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readBoolean(),
        parcel.readString()!!
    ) {

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(path)
        parcel.writeString(name)
        parcel.writeString(size)
        parcel.writeString(time)
        parcel.writeBoolean(piority)
        parcel.writeString(timeEdit)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PdfFile> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): PdfFile {
            return PdfFile(parcel)
        }

        override fun newArray(size: Int): Array<PdfFile?> {
            return arrayOfNulls(size)
        }
    }

}