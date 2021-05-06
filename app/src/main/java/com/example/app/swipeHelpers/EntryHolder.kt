package com.example.app.swipeHelpers

import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout

interface EntryHolder {

    fun getDeleteIcon() : ImageView

    fun getEditIcon() : ImageView

    fun getEntryBackground() : ConstraintLayout

    fun getEntryForeground() : ConstraintLayout
}