package com.happyplaces.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.app.*
import com.example.app.crypto.Decrypter
import com.example.app.database.DatabaseIdentity
import com.example.app.models.IdentityModel
import kotlinx.android.synthetic.main.item_identity.view.*

open class IdentityAdapter(

    private val context: Context,
    private var list: ArrayList<IdentityModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_identity,
                parent,
                false
            )
        )
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        val d = Decrypter(model.iv)

        if (holder is ViewHolder) {
            holder.name.text = d.decryptString(model.name)
            holder.email.text = d.decryptString(model.email)
            holder.surname = d.decryptString(model.surname)
            holder.country = d.decryptString(model.country)
            holder.street = d.decryptString(model.street)
            holder.phone = d.decryptString(model.phoneNumber)
            holder.app = d.decryptString(model.app)
            holder.postcode = d.decryptString(model.postcode)
            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.OnClick(position, model)
                }
            }
        }
    }

    interface OnClickListener {
        fun OnClick(position: Int, model: IdentityModel)
    }

    fun updateIdentity(holder: IdentityAdapter.ViewHolder, model: IdentityModel) {
        if (DatabaseIdentity(context).updateIdentity(
                IdentityModel(list[holder.adapterPosition].id, model.name, model.surname, model.street,
                model.app, model.country, model.postcode, model.phoneNumber, model.email, model.iv)
            ) == -1)
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        else {
            Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
            if (context is IdentityActivity)
                context.getIdentitiesListFromPrivateDB()
        }
    }

    fun deleteIdentity(holder: ViewHolder) {
        val dbHandler = DatabaseIdentity(context)
        val isDeleted = dbHandler.deleteIdentity(list[holder.adapterPosition])
        if (isDeleted > 0) {
            list.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val title: TextView = view.tv_title_item_identity
        val email: TextView = view.tv_email_item_identity
        val name: TextView = view.tv_title_item_identity
        var surname: String = ""
        var street: String = ""
        var app: String = ""
        var postcode: String = ""
        var country: String = ""
        var phone: String = ""
        /*val surname: TextView = TextView()
        val street: TextView = view.tvStreet
        val app: TextView = view.tvApp
        val postcode: TextView = view.tvPostcode
        val country: TextView = view.tvCountry
        val phone: TextView = view.tvPhone
        */
        val background: ConstraintLayout = view.identity_background
        val foreground: CardView = view.identity_foreground
    }
}
// END