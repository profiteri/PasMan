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
import com.example.app.AddNoteActivity
import com.example.app.NotesActivity
import com.example.app.models.NoteModel
import com.example.app.R
import com.example.app.adapters.ProfilesAdapter
import com.example.app.crypto.Decrypter
import com.example.app.database.DatabaseNotes
import com.example.app.models.IdentityModel
import kotlinx.android.synthetic.main.activity_notes.view.*
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
            holder.text.text = d.decryptString(model.text)
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

    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int) {
        val intent = Intent(context, AddNoteActivity::class.java)
        intent.putExtra(NotesActivity.EXTRA_NOTES_DETAILS, list[position])
        activity.startActivityForResult(intent, requestCode)
        notifyItemChanged(position)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val title : TextView = view.tv_title_item
        val text : TextView = view.tv_shorttext_item
        val background: ConstraintLayout = view.findViewById(R.id.notes_background)
        val foreground: CardView = view.findViewById(R.id.notes_foreground)
    }
}
// END