package com.example.app.adapters

import com.example.app.models.CardModel


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
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
import com.example.app.swipeHelpers.AdapterHolder
import com.example.app.swipeHelpers.EntryHolder
import kotlinx.android.synthetic.main.item_card.view.*

class CardsAdapter(val context: Context, val items: ArrayList<CardModel>) :
    RecyclerView.Adapter<CardsAdapter.ViewHolder>(), AdapterHolder {

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

    private fun deleteCard(holder: ViewHolder) {
        val handler = DatabaseCards(context)
        if (handler.deleteCard(items[holder.adapterPosition]) == -1) {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
        if (context is CardActivity)
            context.setupListOfDataIntoRecycleView()
    }

    override fun deleteItem(holder: RecyclerView.ViewHolder) {
        deleteCard(holder as ViewHolder)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), EntryHolder {
        val mainLayout: FrameLayout = view.main_layout_card
        val number: TextView = view.tvNumber
        val holder: TextView = view.tvHolder
        val expiry: TextView = view.tvExpiry
        val cvc: TextView = view.tvCVC
        val pin: TextView = view.tvPIN
        val comment: TextView = view.tvComment
        //val background: ConstraintLayout = view.card_background
        val foreground: ConstraintLayout = view.card_foreground
        val backside: ConstraintLayout = view.card_backside

        override fun getDeleteIcon(): ImageView {
            //return background.icon_delete
            return ImageView(null)
        }

        override fun getEditIcon(): ImageView {
            //return background.icon_eye
            return ImageView(null)
        }



        override fun getEntryBackground(): ConstraintLayout {
            return foreground
        }



        override fun getEntryForeground(): ConstraintLayout {
            return foreground
        }
    }

}