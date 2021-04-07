package com.example.app.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.ProfileActivity
import com.example.app.R
import com.example.app.adapters.ProfilesAdapter
import kotlinx.android.synthetic.main.activity_profile.*

class DeleteProfile(private val adapter: ProfilesAdapter,
                    private val holder: ProfilesAdapter.ViewHolder,
                    private val profile: RecyclerView, context: Context) : DialogFragment() {

    var cancel = false
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.delete_profile_dialog)
                .setPositiveButton(R.string.delete,
                    DialogInterface.OnClickListener { dialog, id ->
                        //val adapter = rv_profiles.adapter as ProfilesAdapter
                        adapter.deleteProfile(holder)

                    })
                .setNegativeButton(
                    R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        (holder as ProfilesAdapter.ViewHolder).foreground.alpha = 1f
                        holder.foreground.animate().translationX(0f)
                        profile.layoutManager = LinearLayoutManager(context)
                        dialog.cancel()
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun showAndReturn(manager: FragmentManager, tag: String?): Boolean {
        super.show(manager, tag)
        return cancel
    }
}