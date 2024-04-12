package com.example.gym_app.Singlton

class TrueOrFalse {
    var boolean= false
     var name :String ?=null
    companion object{
        var instance : TrueOrFalse ?=null
            get() {
                if(field==null){
                    instance = TrueOrFalse()
                }
                return field
            }
            private set
    }
}