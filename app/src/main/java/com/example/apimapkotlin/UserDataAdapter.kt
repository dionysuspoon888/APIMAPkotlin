package com.example.apimapkotlin

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import java.util.ArrayList

/**
 * Created by D on 10/28/2018.
 */


class UserDataAdapter(val ctx:Context , val list:ArrayList<UserData> ) : RecyclerView.Adapter<UserDataAdapter.ViewHolder>() {

    private var listener: OnItemClickListener? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Create ViewHolder when not enough
        val v = LayoutInflater.from(ctx).inflate(R.layout.item_user, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //Define the data shows in the card View
        val currentItem = list[position]


        val latitude = currentItem.latitude
        val longitude = currentItem.longitude

        val picture = currentItem.picture
        val _id = currentItem._id
        val name = currentItem.name
        val email = currentItem.email


        Glide.with(ctx).load(picture).into(holder.iv_user_image)

        holder.tv_user_name.setText(name)

        holder.tv_user_email.setText(email)


    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //ViewHolder set up(mandatory for RecyclerView)
        val iv_user_image: ImageView
        val tv_user_name: TextView
        val tv_user_email: TextView


        init {
            iv_user_image = itemView.findViewById(R.id.iv_user_image)
            tv_user_name = itemView.findViewById(R.id.tv_user_name)
            tv_user_email = itemView.findViewById(R.id.tv_user_email)


            itemView.setOnClickListener {
                if (listener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener!!.onItemClick(position)
                    }
                }
            }

        }
    }

    //OnClick UI

    fun setOnItemClickListener(listeners: OnItemClickListener) {
        listener = listeners
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)

    }


}
