package com.example.app.SwipeHelpers

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.adapters.CardsAdapter
import com.example.app.adapters.ProfilesAdapter
import kotlinx.android.synthetic.main.item_profile.view.*
import kotlin.math.abs

abstract class CardSwipeHelper(private val dir: Int)
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
            if (dir == ItemTouchHelper.RIGHT) {
                (viewHolder as CardsAdapter.ViewHolder).background.setBackgroundResource(R.drawable.card_background_edit)
                viewHolder.background.icon_delete.visibility = View.GONE
                viewHolder.background.icon_eye.visibility = View.VISIBLE
            }
            else {
                (viewHolder as ProfilesAdapter.ViewHolder).background.setBackgroundResource(R.drawable.card_background)
                viewHolder.background.icon_delete.visibility = View.VISIBLE
                viewHolder.background.icon_eye.visibility = View.GONE
            }
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected((viewHolder as CardsAdapter.ViewHolder).foreground)
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
        (viewHolder as CardsAdapter.ViewHolder).foreground.alpha = 1 - abs(dX/viewHolder.itemView.width)
        getDefaultUIUtil().onDraw(c, recyclerView,
            viewHolder.foreground, dX, dY,
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
        getDefaultUIUtil().clearView((viewHolder as CardsAdapter.ViewHolder).foreground)
    }

}