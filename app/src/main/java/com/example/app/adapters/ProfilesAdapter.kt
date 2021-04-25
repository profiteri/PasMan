package com.example.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.app.ProfileActivity
import com.example.app.R
import com.example.app.crypto.Decrypter
import com.example.app.database.DatabaseProfile
import com.example.app.models.ProfileModel
import java.security.KeyStore
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
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


    @Synchronized
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        val d = Decrypter(item.iv)

        holder.source.text = d.decryptString(item.source)
        holder.login.text = d.decryptString(item.login)
        holder.password.text = d.decryptString(item.password)
        holder.info.text = d.decryptString(item.info)
        holder.icon.setImageResource(when((holder.source.text as String).toLowerCase(Locale.ROOT)){
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

    }

    fun deleteProfile(holder: ViewHolder) {
        if (DatabaseProfile(context).deleteProfile(items[holder.adapterPosition]) == -1) {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
       if (context is ProfileActivity)
           context.setupListOfDataIntoRecycleView()
    }

    fun updateProfile(holder: ViewHolder, profileModel: ProfileModel) {
        if (DatabaseProfile(context).updateProfile(ProfileModel(items[holder.adapterPosition].id,
                profileModel.source,
                profileModel.login,
                profileModel.password,
                profileModel.info,
                profileModel.iv)) == -1) {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
            if (context is ProfileActivity) {
                context.setupListOfDataIntoRecycleView()
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
        val source: TextView = view.findViewById<TextView>(R.id.tvSource)
        val icon: ImageView = view.findViewById<ImageView>(R.id.icon_profile)
        val login: TextView = view.findViewById<TextView>(R.id.tvLogin)
        val password: TextView = view.findViewById<TextView>(R.id.tvPassword)
        val info: TextView = view.findViewById<TextView>(R.id.tvInfo)
        val delete: Button = view.findViewById<Button>(R.id.buDelete)
        val background: ConstraintLayout = view.findViewById(R.id.card_background)
        val foreground: CardView = view.findViewById(R.id.card_foreground)
    }

}