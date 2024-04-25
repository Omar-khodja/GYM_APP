package com.example.gym_app.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gym_app.Adapter.Client_Progress_Adapter
import com.example.gym_app.Client_Progress_Data
import com.example.gym_app.R
import com.example.gym_app.Singlton.User
import com.example.gym_app.databinding.FragmentClientProgresseBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class Client_Progresse_Fragment : Fragment() {
    lateinit var biding : FragmentClientProgresseBinding
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        biding = FragmentClientProgresseBinding.inflate(inflater,container,false)

        var collection = db.collection("Client_Progress")
            .document(User.instance?.UserId.toString())
            .collection("Client")
        getProgress(collection)


        return biding.root
    }

    private fun getProgress(collection: Query) {
        var itemlist = mutableListOf<Client_Progress_Data>()
        collection.get().addOnSuccessListener {
            for(doc in it.documents){
                var Title = doc.getString("Title").toString()
                var goal = doc.getString("goal").toString()
                var progress = doc.getString("progress").toString()
                var imagUrl = doc.getString("ClientImag").toString()
                var ClientName = doc.getString("ClientName").toString()
                itemlist.add(Client_Progress_Data(ClientName,imagUrl,Title,progress,goal))
                val adapter = Client_Progress_Adapter(itemlist)
                biding.RecyclerView.adapter = adapter


            }
        }

    }

}