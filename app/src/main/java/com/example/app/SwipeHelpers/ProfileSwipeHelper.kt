package com.example.app.SwipeHelpers

import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.adapters.ProfilesAdapter
import kotlinx.android.synthetic.main.item_profile.view.*

abstract class ProfileSwipeHelper(private val dir: Int) : ItemTouchHelper.SimpleCallback(0, dir)  {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val i = 1
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null) {
            if (dir == ItemTouchHelper.RIGHT) {
                (viewHolder as ProfilesAdapter.ViewHolder).background.setBackgroundResource(R.drawable.card_background_edit)
                viewHolder.background.icon_delete.visibility = View.GONE
                viewHolder.background.icon_eye.visibility = View.VISIBLE
            }
            else {
                (viewHolder as ProfilesAdapter.ViewHolder).background.setBackgroundResource(R.drawable.card_background)
                viewHolder.background.icon_delete.visibility = View.VISIBLE
                viewHolder.background.icon_eye.visibility = View.GONE
            }
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(viewHolder.foreground)
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
        (viewHolder as ProfilesAdapter.ViewHolder).foreground.alpha = 1 - Math.abs(dX/viewHolder.itemView.width)
        getDefaultUIUtil().onDraw(c, recyclerView,
            (viewHolder as ProfilesAdapter.ViewHolder).foreground, dX, dY,
            actionState, isCurrentlyActive)
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
        getDefaultUIUtil().clearView((viewHolder as ProfilesAdapter.ViewHolder).foreground)
    }

}