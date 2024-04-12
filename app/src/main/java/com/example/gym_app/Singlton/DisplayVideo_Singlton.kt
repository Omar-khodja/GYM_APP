package com.example.gym_app.Singlton

class DisplayVideo_Singlton{
     var title: String ?=null
     var des: String ?=null
     var videoUrl: String ?=null
     var imagUrl:String ?=null
    companion object{
        var instance: DisplayVideo_Singlton ?=null
            get() {
                if(field == null){
                    instance = DisplayVideo_Singlton()
                }
                return field
            }
            private set
    }

}