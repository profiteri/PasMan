package com.example.app.SwipeHelpers

import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
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

    private fun determineClass(viewHolder: RecyclerView.ViewHolder?) {
        profileHolder = try {
            viewHolder as ProfilesAdapter.ViewHolder
        } catch (e : ClassCastException) {null}
        cardsHolder = try {
            viewHolder as CardsAdapter.ViewHolder
        } catch (e : ClassCastException) {null}
        notesHolder = try {
            viewHolder as NotesAdapter.ViewHolder
        } catch (e : ClassCastException) {null}
        identityHolder = try {
            viewHolder as IdentityAdapter.ViewHolder
        } catch (e : ClassCastException) {null}
    }

    private var profileHolder : ProfilesAdapter.ViewHolder? = null
    private var cardsHolder : CardsAdapter.ViewHolder? = null
    private var notesHolder : NotesAdapter.ViewHolder? = null
    private var identityHolder : IdentityAdapter.ViewHolder? = null

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        determineClass(viewHolder)
        profileHolder?.let {
            if (dir == ItemTouchHelper.RIGHT) {
                it.background.visibility = View.VISIBLE
                it.background.setBackgroundResource(R.drawable.card_background_edit)
                it.background.icon_delete.visibility = View.GONE
                it.background.icon_eye.visibility = View.VISIBLE
            }
            else {
                it.background.visibility = View.VISIBLE
                it.background.setBackgroundResource(R.drawable.card_background)
                it.background.icon_delete.visibility = View.VISIBLE
                it.background.icon_eye.visibility = View.GONE
            }
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(it.foreground)
        }
        notesHolder?.let {
            if (dir == ItemTouchHelper.RIGHT) {
                it.background.setBackgroundResource(R.drawable.card_background_edit)
                it.background.icon_delete.visibility = View.GONE
                it.background.icon_eye.visibility = View.VISIBLE
            }
            else {
                it.background.setBackgroundResource(R.drawable.card_background)
                it.background.icon_delete.visibility = View.VISIBLE
                it.background.icon_eye.visibility = View.GONE
            }
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(it.foreground)
        }
        identityHolder?.let {
            if (dir == ItemTouchHelper.RIGHT) {
                it.background.setBackgroundResource(R.drawable.card_background_edit)
                it.background.icon_delete.visibility = View.GONE
                it.background.icon_eye.visibility = View.VISIBLE
            }
            else {
                it.background.setBackgroundResource(R.drawable.card_background)
                it.background.icon_delete.visibility = View.VISIBLE
                it.background.icon_eye.visibility = View.GONE
            }
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(it.foreground)
        }
        cardsHolder?.let {
            if (dir == ItemTouchHelper.RIGHT) {
                it.background.setBackgroundResource(R.drawable.card_background_edit)
                it.background.icon_delete.visibility = View.GONE
                it.background.icon_eye.visibility = View.VISIBLE
            }
            else {
                it.background.setBackgroundResource(R.drawable.card_background)
                it.background.icon_delete.visibility = View.VISIBLE
                it.background.icon_eye.visibility = View.GONE
            }
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(it.foreground)
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
        determineClass(viewHolder)
        profileHolder?.let {
            it.background.alpha = abs(dX / viewHolder.itemView.width)
            it.foreground.alpha =
                1 - abs(dX / viewHolder.itemView.width)
            getDefaultUIUtil().onDraw(
                c, recyclerView,
                it.foreground, dX, dY,
                actionState, isCurrentlyActive
            )
        }
        notesHolder?.let {
            it.foreground.alpha =
                1 - abs(dX / viewHolder.itemView.width)
            getDefaultUIUtil().onDraw(
                c, recyclerView,
                it.foreground, dX, dY,
                actionState, isCurrentlyActive
            )
        }
        cardsHolder?.let {
            it.foreground.alpha =
                1 - abs(dX / viewHolder.itemView.width)
            getDefaultUIUtil().onDraw(
                c, recyclerView,
                it.foreground, dX, dY,
                actionState, isCurrentlyActive
            )
        }
        identityHolder?.let {
            it.foreground.alpha =
                1 - abs(dX / viewHolder.itemView.width)
            getDefaultUIUtil().onDraw(
                c, recyclerView,
                it.foreground, dX, dY,
                actionState, isCurrentlyActive
            )
        }
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
        profileHolder?.let {
            getDefaultUIUtil().clearView(it.foreground)
            it.background.visibility = View.INVISIBLE
        }
        notesHolder?.let {
            getDefaultUIUtil().clearView(it.foreground)
        }
        cardsHolder?.let {
            getDefaultUIUtil().clearView(it.foreground)
        }
        identityHolder?.let {
            getDefaultUIUtil().clearView(it.foreground)
        }
    }

}