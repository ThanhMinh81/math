package com.example.matheasyapp.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "history_table")
class History() : Parcelable {

    constructor(result: String, calculation: String ) : this() {
        this.result = result
        this.calculation = calculation
    }

    @PrimaryKey(autoGenerate = true)
    var id = 0

    var calculation: String? = null

    var result: String? = null

    @Ignore
    var selected: Boolean = false // Ví dụ về một trường không được thêm vào cơ sở dữ liệu

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        calculation = parcel.readString()
        result = parcel.readString()
        selected = parcel.readByte() != 0.toByte()

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(calculation)
        parcel.writeString(result)
        parcel.writeByte(if (selected) 1 else 0)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<History> {
        override fun createFromParcel(parcel: Parcel): History {
            return History(parcel)
        }

        override fun newArray(size: Int): Array<History?> {
            return arrayOfNulls(size)
        }
    }


}