package com.example.app.adapters

import com.example.app.models.CardModel


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.app.CardActivity
import com.example.app.ProfileActivity
import com.example.app.R
import com.example.app.crypto.Decrypter
import com.example.app.database.DatabaseCards
import com.example.app.database.DatabaseProfile
import com.example.app.models.ProfileModel

class CardsAdapter(val context: Context, val items: ArrayList<CardModel>) :
    RecyclerView.Adapter<CardsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]

        val d = Decrypter(item.iv)

        holder.number.text = d.decryptString(item.number)
        holder.holder.text = d.decryptString(item.holder)
        holder.expiry.text = d.decryptString(item.expiry)
        holder.cvc.text = d.decryptString(item.cvc)
        holder.pin.text = d.decryptString(item.pin)
        holder.comment.text = d.decryptString(item.comment)
    }

    fun updateCard(holder: ViewHolder, cardModel: CardModel) {
        if (DatabaseCards(context).updateCard(
                CardModel(
                    items[holder.adapterPosition].id,
                    cardModel.number,
                    cardModel.holder,
                    cardModel.expiry,
                    cardModel.cvc,
                    cardModel.pin,
                    cardModel.comment,
                    cardModel.iv
                )
            ) == -1
        ) {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
        if (context is CardActivity) {
            context.setupListOfDataIntoRecycleView()
        }
    }

    fun deleteCard(holder: ViewHolder) {
        val handler = DatabaseCards(context)
        if (handler.deleteCard(items[holder.adapterPosition]) == -1) {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
        if (context is CardActivity)
            context.setupListOfDataIntoRecycleView()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val number: TextView = view.findViewById<TextView>(R.id.tvNumber)
        val holder: TextView = view.findViewById<TextView>(R.id.tvHolder)
        val expiry: TextView = view.findViewById<TextView>(R.id.tvExpiry)
        val cvc: TextView = view.findViewById<TextView>(R.id.tvCVC)
        val pin: TextView = view.findViewById<TextView>(R.id.tvPIN)
        val comment: TextView = view.findViewById<TextView>(R.id.tvComment)
        val background: ConstraintLayout = view.findViewById(R.id.card_background)
        val foreground: CardView = view.findViewById(R.id.card_foreground)
    }
}