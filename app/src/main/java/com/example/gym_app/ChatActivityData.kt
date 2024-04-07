package com.example.gym_app

import android.net.Uri

class ChatActivityData(
    val Id: String?,
    val imagUri: Uri,
    val mesg:String?,
    val sender:String?,
    val fromId:String?,
    val toId:String?,
    val time: String?
) {
}