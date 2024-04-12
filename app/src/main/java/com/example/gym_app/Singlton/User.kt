package com.example.gym_app.Singlton

import android.net.Uri

class User {
     var UserId: String ?= null
     var Email: String ?= null
     var UserName: String ?= null
     var UserPhonenb: String ?= null
     var CoachOrClient: String ?= null
     var ProfileimagUri: Uri ?= null
    companion object {
        var instance: User? = null
            get(){
        if(field == null)
        {
            field = User()
        }
                return  field
    }
            private set
    }

}
