package com.example.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.app.ProfileActivity
import com.example.app.R
import com.example.app.models.ProfileModel
import java.util.*
import kotlin.collections.ArrayList

class ProfilesAdapter(val context: Context, val items: ArrayList<ProfileModel>) :
    RecyclerView.Adapter<ProfilesAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilesAdapter.ViewHolder {
        return ProfilesAdapter.ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_profile,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.source.text = item.source
        holder.login.text = item.login
        holder.password.text = item.password
        holder.info.text = item.info
        holder.icon.setImageResource(when(item.source.toLowerCase(Locale.ROOT)){
            "amazon" -> R.drawable.icon_amazon
            "adobe" -> R.drawable.icon_adobe
            "facebook" -> R.drawable.icon_facebook
            "linkedIn" -> R.drawable.icon_linkedin
            "instagram" -> R.drawable.icon_instagram
            "tiktok" -> R.drawable.icon_tiktok
            "google" -> R.drawable.icon_google
            "twitter" -> R.drawable.icon_twitter
            "vk" -> R.drawable.icon_vk
            else -> R.drawable.all
        })



      //  holder.delete.setOnClickListener {
      //      if (context is ProfileActivity)
      //          context.deleteItem(item)
      //  }
    }

    fun deleteProfile(holder: ViewHolder) {
       if (context is ProfileActivity)
           context.deleteItem(items[holder.adapterPosition])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
        val source: TextView = view.findViewById<TextView>(R.id.tvSource)
        val icon: ImageView = view.findViewById<ImageView>(R.id.icon_profile)
        val login: TextView = view.findViewById<TextView>(R.id.tvLogin)
        val password: TextView = view.findViewById<TextView>(R.id.tvPassword)
        val info: TextView = view.findViewById<TextView>(R.id.tvInfo)
        val delete: Button = view.findViewById<Button>(R.id.buDelete)
        val background: RelativeLayout = view.findViewById(R.id.card_background)
        val foreground: CardView = view.findViewById(R.id.card_foreground)
    }

}