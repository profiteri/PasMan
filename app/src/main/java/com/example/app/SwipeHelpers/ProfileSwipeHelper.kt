package com.example.app.SwipeHelpers

import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.app.adapters.ProfilesAdapter

abstract class ProfileSwipeHelper : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT)  {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null) {
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected((viewHolder as ProfilesAdapter.ViewHolder).foreground)
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
        //if (a)
        //    params = (viewHolder as ProfilesAdapter.ViewHolder).itemView.layoutParams
        //a = false
        ///System.out.println((viewHolder as ProfilesAdapter.ViewHolder).foreground.translationX)
        //System.out.println((viewHolder as ProfilesAdapter.ViewHolder).foreground.x)
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

    var bol = false
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        getDefaultUIUtil().clearView((viewHolder as ProfilesAdapter.ViewHolder).foreground)
    }

}