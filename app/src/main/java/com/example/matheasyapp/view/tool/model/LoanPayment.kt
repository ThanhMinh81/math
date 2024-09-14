package com.example.matheasyapp.view.tool.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey


class LoanPayment(
    var month: String,
    var principal: String,
    var interest: String,
    var payment: String,
    var balance: String, ) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
    ) {
    }

    override fun toString(): String {
        return "LoanPayment(month=$month, principal=$principal, interest=$interest, payment=$payment, balance=$balance)"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(month)
        parcel.writeString(principal)
        parcel.writeString(interest)
        parcel.writeString(payment)
        parcel.writeString(balance)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoanPayment> {
        override fun createFromParcel(parcel: Parcel): LoanPayment {
            return LoanPayment(parcel)
        }

        override fun newArray(size: Int): Array<LoanPayment?> {
            return arrayOfNulls(size)
        }
    }


}
