package com.example.app

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

open abstract class ButtonsFunctionality : AppCompatActivity() {
    var addMenuOpen = true
    var switchMenuOpen = false
    fun rotate(btn: ImageButton) {
        val aniRotate: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate)
        btn.startAnimation(aniRotate)
    }

    fun selectMenu(btn_angle: Button, iv_angle_image: ImageView, ll_layout_menu: LinearLayout) {
        val animator = ObjectAnimator.ofFloat(iv_angle_image, View.TRANSLATION_Y, -10f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.duration = 100;
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                btn_angle.isEnabled = false;
            }

            override fun onAnimationEnd(animation: Animator?) {
                btn_angle.isEnabled = true;
            }
        })
        animator.start()

        if (!switchMenuOpen) {
            ll_layout_menu.visibility = View.VISIBLE
            switchMenuOpen = true
        } else {
            ll_layout_menu.visibility = View.GONE
            switchMenuOpen = false
        }
    }

    fun plusButton(
        view: View,
        iv_icon: Int,
        iv_plus_image: ImageView,
        main_layout: Int,
        ll_add_menu: Int,
        toCenter: Boolean
    ) {
        val animator = ObjectAnimator.ofFloat(iv_plus_image, View.ROTATION, 135f)
        animator.duration = 300;
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled = false;
            }

            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled = true;

            }
        })//вращение

        val c = ConstraintSet()
        c.clone(findViewById<ConstraintLayout>(main_layout))
        val transition = AutoTransition()
        transition.duration = 300
        transition.interpolator = AccelerateDecelerateInterpolator()
        if (addMenuOpen) {
            addMenuOpen = false
            if (toCenter) {
                c.clear(ll_add_menu, ConstraintSet.TOP)
                c.connect(ll_add_menu, ConstraintSet.TOP, main_layout, ConstraintSet.TOP)
                c.connect(ll_add_menu, ConstraintSet.BOTTOM, main_layout, ConstraintSet.BOTTOM)
            }
            else
                c.connect(ll_add_menu, ConstraintSet.TOP, iv_icon, ConstraintSet.BOTTOM)
            animator.start()
        } else {
            addMenuOpen = true
            if (toCenter)
                c.clear(ll_add_menu, ConstraintSet.BOTTOM)
            c.connect(ll_add_menu, ConstraintSet.TOP, main_layout, ConstraintSet.BOTTOM)
            animator.setFloatValues(0f)
            animator.start()
        }
        TransitionManager.beginDelayedTransition(findViewById<ConstraintLayout>(main_layout), transition)
        c.applyTo(findViewById<ConstraintLayout>(main_layout)) //появление
    }

    fun toCards(view: View) {
        val intent = Intent(this, CardActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun toAccounts(view: View) {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun toIdent(view: View) {
        val intent = Intent(this, IdentityActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun toNotes(view: View) {
        val intent = Intent(this, NotesActivity::class.java)
        startActivity(intent)
        finish()
    }
}