package com.example.app.swipeHelpers

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.adapters.CardsAdapter
import com.example.app.adapters.ProfilesAdapter
import com.happyplaces.adapters.IdentityAdapter
import com.happyplaces.adapters.NotesAdapter
import kotlinx.android.synthetic.main.item_profile.view.*
import java.lang.ClassCastException
import kotlin.math.abs

abstract class ProfileSwipeHelper(private val dir: Int)
    : ItemTouchHelper.SimpleCallback(0, dir)  {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null) {
            val entry = viewHolder as EntryHolder
            entry.getEntryBackground().visibility = View.VISIBLE
            if (dir == ItemTouchHelper.RIGHT) {
                entry.getEntryBackground().setBackgroundResource(R.drawable.card_background_edit)
                entry.getDeleteIcon().visibility = View.GONE
                entry.getEditIcon().visibility = View.VISIBLE
            } else {
                entry.getEntryBackground().setBackgroundResource(R.drawable.card_background)
                entry.getDeleteIcon().visibility = View.VISIBLE
                entry.getEditIcon().visibility = View.GONE
            }
        }
    }


    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val entry = viewHolder as EntryHolder
        val newAlpha = abs(dX / viewHolder.itemView.width)
        entry.getEntryBackground().alpha = newAlpha
        entry.getEntryForeground().alpha = 1 - newAlpha
        getDefaultUIUtil().onDraw(
            c, recyclerView,
            entry.getEntryForeground(), dX, dY,
            actionState, isCurrentlyActive
        )
    }

    /*override fun onChildDrawOver(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder?,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        getDefaultUIUtil().onDrawOver(
            c, recyclerView,
            (viewHolder as ProfilesAdapter.ViewHolder).foreground, dX/4, dY,
            actionState, isCurrentlyActive)
    }*/

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        val entry = viewHolder as EntryHolder
        getDefaultUIUtil().clearView(entry.getEntryForeground())
        entry.getEntryBackground().visibility = View.INVISIBLE
    }

}