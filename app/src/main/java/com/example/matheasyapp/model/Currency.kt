package com.example.matheasyapp.model

import android.os.Parcel
import android.os.Parcelable
import java.math.BigDecimal

class Currency(
    val symbol: String,
    val name: String,
    val rate: Double,
    var check: Boolean,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
         parcel.readDouble().toDouble() ,
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(symbol)
        parcel.writeString(name)
         parcel.writeDouble(rate)
        parcel.writeByte(if (check) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Currency> {
        override fun createFromParcel(parcel: Parcel): Currency {
            return Currency(parcel)
        }

        override fun newArray(size: Int): Array<Currency?> {
            return arrayOfNulls(size)
        }
    }
}
