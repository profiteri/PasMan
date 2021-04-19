package com.example.app.SwipeHelpers

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.adapters.CardsAdapter
import com.example.app.adapters.ProfilesAdapter
import com.happyplaces.adapters.IdentityAdapter
import com.happyplaces.adapters.NotesAdapter
import kotlinx.android.synthetic.main.activity_profile.*
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
            var profileHolder : ProfilesAdapter.ViewHolder?
            var cardsHolder : CardsAdapter.ViewHolder?
            var notesHolder : NotesAdapter.ViewHolder?
            var identityHolder : IdentityAdapter.ViewHolder?

            var profileAdapter : ProfilesAdapter?
            var cardsAdapter : CardsAdapter?
            var notesAdapter : NotesAdapter?
            var identityAdapter : IdentityAdapter?

            try {
                profileAdapter = recyclerView.adapter as ProfilesAdapter
                profileHolder = holder as ProfilesAdapter.ViewHolder
            } catch (e : ClassCastException) {
                profileAdapter = null
                profileHolder = null
            }
            try {
                cardsAdapter = recyclerView.adapter as CardsAdapter
                cardsHolder = holder as CardsAdapter.ViewHolder
            } catch (e : ClassCastException) {
                cardsAdapter = null
                cardsHolder = null
            }
            try {
                notesAdapter = recyclerView.adapter as NotesAdapter
                notesHolder = holder as NotesAdapter.ViewHolder
            } catch (e : ClassCastException) {
                notesAdapter = null
                notesHolder = null
            }
            try {
                identityAdapter = recyclerView.adapter as IdentityAdapter
                identityHolder = holder as IdentityAdapter.ViewHolder
            } catch (e : ClassCastException) {
                identityAdapter = null
                identityHolder = null
            }

            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.delete_profile_dialog)
                .setPositiveButton(
                    R.string.delete
                ) { _, _ ->
                    profileHolder?.let { profileAdapter?.deleteProfile(it) }
                    cardsHolder?.let {cardsAdapter?.deleteCard(it) }
                    notesHolder?.let { notesAdapter?.deleteNote(it) }
                    identityHolder?.let { identityAdapter?.deleteIdentity(it) }
                }
                .setNegativeButton(
                    R.string.cancel
                ) { dialog, _ ->
                    val animatorListener = object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            recyclerView.layoutManager = LinearLayoutManager(context)
                        }
                    }
                    profileHolder?.let {
                        it.foreground.alpha = 1f
                        it.foreground.animate().translationX(0f).setListener(animatorListener).start()
                    }
                    cardsHolder?.let {
                        it.foreground.alpha = 1f
                        it.foreground.animate().translationX(0f).setListener(animatorListener).start()
                    }
                    notesHolder?.let {
                        it.foreground.alpha = 1f
                        it.foreground.animate().translationX(0f).setListener(animatorListener).start()
                    }
                    identityHolder?.let {
                        it.foreground.alpha = 1f
                        it.foreground.animate().translationX(0f).setListener(animatorListener).start()
                    }
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    /*fun showAndReturn(manager: FragmentManager, tag: String?): Boolean {
        super.show(manager, tag)
        return cancel
    }*/
}