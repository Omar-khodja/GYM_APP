package com.example.gym_app.Singlton

class ChatOtherUser {
     var otherId:String ?=null
     var MyId:String ?=null
     var imguri:String ?=null
     var username:String ?=null
    companion object {
        var instance: ChatOtherUser? = null
            get(){
                if(field == null){
                    instance = ChatOtherUser()
                }
                return field
            }
            private set
    }
}