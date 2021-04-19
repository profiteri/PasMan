package com.example.app.adapters

import com.example.app.models.CardModel


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.app.CardActivity
import com.example.app.ProfileActivity
import com.example.app.R

class CardsAdapter(val context: Context, val items: ArrayList<CardModel>) :
    RecyclerView.Adapter<CardsAdapter.ViewHolder>() {

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_card,
                parent,
                false
            )
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]
        holder.number.text = item.number
        holder.holder.text = item.holder
        holder.expiry.text = item.expiry
        holder.cvc.text = item.cvc.toString()
        holder.pin.text = item.pin.toString()
        holder.comment.text = item.comment
    }

    fun deleteCard(holder: CardsAdapter.ViewHolder) {
        if (context is CardActivity)
            context.deleteItem(items[holder.adapterPosition])
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
        //val llMain: LinearLayout = view.findViewById<LinearLayout>(R.id.llMain)
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