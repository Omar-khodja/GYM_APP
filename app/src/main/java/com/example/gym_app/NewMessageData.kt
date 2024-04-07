package com.example.gym_app
import android.os.Parcelable

class NewMessageData(
    val userId:String,
    val username:String,
    val imguri: String

) {
    constructor():this("","","")

}