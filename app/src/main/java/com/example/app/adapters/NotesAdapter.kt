package com.happyplaces.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.app.NotesActivity
import com.example.app.models.NoteModel
import com.example.app.R
import com.example.app.swipeHelpers.EntryHolder
import com.example.app.crypto.Decrypter
import com.example.app.database.DatabaseNotes
import kotlinx.android.synthetic.main.item_notes.view.*

open class NotesAdapter(

    private val context: Context,
    private var list: ArrayList<NoteModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_notes,
                parent,
                false
            )
        )
    }

    interface OnClickListener {
        fun OnClick(position: Int, model: NoteModel)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        val d = Decrypter(model.iv)

        if (holder is ViewHolder) {
            holder.title.text = d.decryptString(model.titel)
            holder.text = d.decryptString(model.text)
            holder.shortText.text = holder.text
            //holder.shortText.text = holder.shortText.text.subSequence(
              //  holder.shortText.layout.getLineStart(0),
                //holder.shortText.layout.getLineEnd(0))
            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.OnClick(position, model)
                }
            }
        }
    }

    fun updateNote(holder: ViewHolder, model: NoteModel) {
        if (DatabaseNotes(context).updateNote(NoteModel(list[holder.adapterPosition].id, model.titel, model.text, model.iv)) == -1)
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        else {
            Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
            if (context is NotesActivity)
                context.getNotesListFromPrivateDB()
        }
    }

    fun deleteNote(holder: ViewHolder) {
        val dbHandler = DatabaseNotes(context)
        val isDeleted = dbHandler.deleteNote(list[holder.adapterPosition])
        if (isDeleted > 0) {
            list.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), EntryHolder{
        val title : TextView = view.tv_title_item
        var shortText : TextView = view.tv_shorttext_item
        var text : String = ""
        val background: ConstraintLayout = view.findViewById(R.id.notes_background)
        val foreground: ConstraintLayout = view.findViewById(R.id.notes_foreground)

        override fun getDeleteIcon(): ImageView {
            return background.icon_delete
        }

        override fun getEditIcon(): ImageView {
            return background.icon_eye
        }

        override fun getEntryBackground(): ConstraintLayout {
            return background
        }

        override fun getEntryForeground(): ConstraintLayout {
            return foreground;
        }
    }
}
// END