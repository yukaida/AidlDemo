package com.kaida.aidltest.entity

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class MessageBean(var messageContent: String? = "", var sendState: Boolean = false) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(messageContent)
        parcel.writeByte(if (sendState) 1 else 0)
    }

    fun readFromParcel(parcel: Parcel) {
        this.messageContent = parcel.readString()
        this.sendState = parcel.readByte().equals(1)
    }


    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MessageBean> {
        override fun createFromParcel(parcel: Parcel): MessageBean {
            return MessageBean(parcel)
        }

        override fun newArray(size: Int): Array<MessageBean?> {
            return arrayOfNulls(size)
        }
    }
}
