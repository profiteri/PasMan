package com.example.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.models.ProfileModel

class ProfilesAdapter(val context: Context, val items: ArrayList<ProfileModel>) :
    RecyclerView.Adapter<ProfilesAdapter.ViewHolder>() {
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
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
        val source: TextView = view.findViewById<TextView>(R.id.tvSource)
        val login: TextView = view.findViewById<TextView>(R.id.tvLogin)
        val password: TextView = view.findViewById<TextView>(R.id.tvPassword)
        val info: TextView = view.findViewById<TextView>(R.id.tvInfo)
    }
}