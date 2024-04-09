package com.example.gym_app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gym_app.CardView_CreateData
import com.example.gym_app.databinding.CustomCardviewVideosBinding


class CardView_CreateAdapter(val itemlist:MutableList<CardView_CreateData>, val OnItemClike: (CardView_CreateData)->Unit )
    :RecyclerView.Adapter<CardView_CreateAdapter.ViewHolder>(){
        lateinit var biding:CustomCardviewVideosBinding
        class ViewHolder(val biding:CustomCardviewVideosBinding):RecyclerView.ViewHolder(biding.root){
            fun bind (data: CardView_CreateData){
                biding.cardViewImage.setImageResource(data.imagUri)
                biding.title.text = data.title
                biding.description.text = data.des
                biding.nbvideo.text = data.nbvideos


            }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        biding = CustomCardviewVideosBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder((biding))
    }

    override fun getItemCount(): Int {
        return itemlist.size
   }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var x = itemlist[position]
        holder.bind(x)
        holder.itemView.setOnClickListener {
            OnItemClike(x)
        }

    }
}