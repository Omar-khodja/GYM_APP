package com.example.gym_app.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gym_app.Client_Progress_Data
import com.example.gym_app.databinding.ClientProgressBinding
import com.example.gym_app.databinding.CustomUsersBinding

class Client_Progress_Adapter(val itemlist:MutableList<Client_Progress_Data>)
    :RecyclerView.Adapter<Client_Progress_Adapter.ViewHolder>(){
    lateinit var biding : ClientProgressBinding
        class ViewHolder(private val biding : ClientProgressBinding):RecyclerView.ViewHolder(biding.root){
            fun bind (data : Client_Progress_Data){
                biding.data = data
                Glide.with(biding.root)
                    .load(data.imag)
                    .into(biding.profileimag)
                var p = data.progress.toDouble()
                var g = data.goal.toDouble()
                var result = (p/g)*100
                Log.i("tagy","$result $p $g")
                biding.progressBar.progress = result.toInt()
                biding.progress.text = result.toInt().toString()+"%"

            }
        }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Client_Progress_Adapter.ViewHolder {
        biding = ClientProgressBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(biding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = itemlist[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return  itemlist.size
    }


}