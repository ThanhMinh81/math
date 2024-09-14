package com.example.matheasyapp.view.tool.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loan_result")

class LoanHistory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val paymentMethods : String,
    val borrowPrincipal : String,
    val interest : String, // von goc
    val borrow : String, // lai suat
    val borrowTime : String, // thanh toan
    val borrowInterest : String,
    val interestTime : String ,
    val paymentSum : String,
    val interestPercent : String ,
)  : Parcelable  {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(paymentMethods)
        parcel.writeString(borrowPrincipal)
        parcel.writeString(interest)
        parcel.writeString(borrow)
        parcel.writeString(borrowTime)
        parcel.writeString(borrowInterest)
        parcel.writeString(interestTime)
        parcel.writeString(paymentSum)
        parcel.writeString(interestPercent)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoanHistory> {
        override fun createFromParcel(parcel: Parcel): LoanHistory {
            return LoanHistory(parcel)
        }

        override fun newArray(size: Int): Array<LoanHistory?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "LoanHistory(id=$id, paymentMethods='$paymentMethods', borrowPrincipal='$borrowPrincipal', interest='$interest', borrow='$borrow', borrowTime='$borrowTime', borrowInterest='$borrowInterest', interestTime='$interestTime', paymentSum='$paymentSum' , interestPercent='$interestPercent')"
    }


}

