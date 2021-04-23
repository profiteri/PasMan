package com.happyplaces.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.app.AddNoteActivity
import com.example.app.NotesActivity
import com.example.app.models.NoteModel
import com.example.app.R
import com.example.app.adapters.ProfilesAdapter
import com.example.app.database.DatabaseNotes
import kotlinx.android.synthetic.main.activity_notes.view.*
import kotlinx.android.synthetic.main.item_notes.view.*

// TODO (Step 6: Creating an adapter class for binding it to the recyclerview in the new package which is adapters.)
// START
open class NotesAdapter(

    private val context: Context,
    private var list: ArrayList<NoteModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onClickListener: OnClickListener? = null

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_notes,
                parent,
                false
            )
        )
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
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
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is ViewHolder) {
            holder.itemView.tv_title_item.text = model.titel
            holder.itemView.tv_shorttext_item.text = model.text
            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.OnClick(position, model)
                }
            }
        }
    }

    interface OnClickListener {
        fun OnClick(position: Int, model: NoteModel)
    }

    /**
     * Gets the number of items in the list
     */


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

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val title : TextView = view.tv_title_item
        val text : TextView = view.tv_shorttext_item
        val background: ConstraintLayout = view.findViewById(R.id.notes_background)
        val foreground: CardView = view.findViewById(R.id.notes_foreground)
    }
}
// END