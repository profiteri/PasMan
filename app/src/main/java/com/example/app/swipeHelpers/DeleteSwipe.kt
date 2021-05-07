package com.example.app.swipeHelpers

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.adapters.CardsAdapter
import com.example.app.adapters.ProfilesAdapter
import com.happyplaces.adapters.IdentityAdapter
import com.happyplaces.adapters.NotesAdapter
import java.lang.ClassCastException

class DeleteSwipe(private val paramsHolder: SwipeParamsHolder) : ProfileSwipeHelper(ItemTouchHelper.LEFT) {
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        DeleteProfile(
            viewHolder,
            paramsHolder.recyclerView
        ).show(paramsHolder.supportFragmentManager, "delete_dialog")
    }
}

class DeleteProfile(private val holder: RecyclerView.ViewHolder,
                    private val recyclerView: RecyclerView
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->

            val adapter = recyclerView.adapter as AdapterHolder

            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.delete_profile_dialog)
                .setPositiveButton(
                    R.string.delete
                ) { _, _ ->
                    adapter.deleteItem(holder)
                }
                .setNegativeButton(
                    R.string.cancel
                ) { dialog, _ ->
                    val animatorListener = object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            recyclerView.layoutManager = LinearLayoutManager(context)
                        }
                    }
                    val item = holder as EntryHolder
                    item.getEntryForeground().apply {
                        alpha = 1f
                        animate().translationX(0f).setListener(animatorListener).start()
                    }
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}