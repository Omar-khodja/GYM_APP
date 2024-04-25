package com.example.gym_app.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gym_app.ChatActivityData
import com.example.gym_app.databinding.ChatOneBinding
import com.example.gym_app.databinding.ChatTowBinding

class ChatOneAdapter(val context: Context,val itemlist:MutableList<ChatActivityData>)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        lateinit var binding : ChatOneBinding
    lateinit var binding1 : ChatTowBinding
    private val VIEW_TYPE_LEFT = 1
    private val VIEW_TYPE_RIGHT = 2
    inner class LeftViewHolder(var biding:ChatOneBinding ) : RecyclerView.ViewHolder(biding.root) {
        fun bind(chat: ChatActivityData){
            biding.time.text = chat.time
            biding.chatmessage.text = chat.mesg
            Glide.with(biding.root)
                .load(Uri.parse(chat.imagUri))
                .into(biding.profileimg)
        }
    }

    inner class RightViewHolder(var biding:ChatTowBinding) : RecyclerView.ViewHolder(biding.root) {
        fun bind(chat: ChatActivityData){
            biding.time.text = chat.time
            biding.chatmessage.text = chat.mesg
            Glide.with(biding.root)
                .load(chat.imagUri)
                .into(biding.profileimg)

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder {
        return if(viewType == VIEW_TYPE_LEFT){
            binding = ChatOneBinding.inflate(
                LayoutInflater.from(parent.context)
                ,parent
                ,false
            )
            LeftViewHolder(binding)
        }else{
            binding1 = ChatTowBinding.inflate(
                LayoutInflater.from(parent.context)
                ,parent
                ,false
            )
            RightViewHolder(binding1)
        }

    }

    override fun getItemCount(): Int {
        return itemlist.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = itemlist[position]
        if (getItemViewType(position) == VIEW_TYPE_LEFT) {
            (holder as LeftViewHolder).bind(message)
        } else {
            (holder as RightViewHolder).bind(message)
        }
    }
    override fun getItemViewType(position: Int): Int {
        return if (itemlist[position].sender == "LEFT") {
            VIEW_TYPE_LEFT
        } else if(itemlist[position].sender == "RIGHT") {
            VIEW_TYPE_RIGHT
        }else
            return error(0)
    }
    }

