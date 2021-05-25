package com.example.app

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.animation.doOnEnd
import kotlinx.android.synthetic.main.activity_cards.*

open class ButtonsFunctionality : AppCompatActivity() {

    var addMenuOpen = -1

    var switchMenuOpen = false

    companion object{
        const val BUTTONS_TRANSLATION_DURATION : Long = 500
        const val BUTTONS_TRANSLATION_X : Float = 200f
        const val MENU_ALPHA_DURATION : Long = 200
    }

    fun rotate(btn: ImageButton) {
        val aniRotate: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate)
        btn.startAnimation(aniRotate)
    }

    fun selectMenu(angleButton : Button, angleImage : ImageView, menu : LinearLayout) {
        val animator = ObjectAnimator.ofFloat(angleImage, View.TRANSLATION_Y, -10f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.duration = 100;
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                angleButton.isEnabled = false;
            }

            override fun onAnimationEnd(animation: Animator?) {
                angleButton.isEnabled = true;
            }
        })
        animator.start()

        switchMenuOpen = if (!switchMenuOpen) {
            wipeOutMenu(1f, menu)
            true
        } else {
            wipeOutMenu(0f, menu)
            false
        }
    }

    private fun wipeOutMenu(alpha : Float, menu : LinearLayout) {
        var visibility : Int? = null

        if (menu.visibility == View.GONE) {
            menu.visibility = View.VISIBLE
            //visibility = View.GONE
        } else {
            //menu.visibility = View.GONE
            visibility = View.VISIBLE
        }

        menu.animate().alpha(alpha).apply {
            duration = MENU_ALPHA_DURATION
            withEndAction { visibility?.let { menu.visibility = it }}
        }.start()
    }

    fun plusButton(
        iv_icon: Int,
        iv_plus_image: Int,
        main_layout: Int,
        ll_add_menu: Int,
        bu_left: Int,
        bu_right: Int
    ) {
        val buttonLeft : ImageButton = findViewById(bu_left)
        val buttonRight : FrameLayout = findViewById(bu_right)
        val plusImage : ImageView = findViewById(iv_plus_image)
        val animator = ObjectAnimator.ofFloat(plusImage, View.ROTATION, 135f)
        animator.duration = 300;
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                plusImage.isEnabled = false;
            }

            override fun onAnimationEnd(animation: Animator?) {
                plusImage.isEnabled = true;

            }
        })//rotation

        val c = ConstraintSet()
        c.clone(findViewById<ConstraintLayout>(main_layout))
        val transition = AutoTransition()
        transition.duration = 300
        transition.interpolator = AccelerateDecelerateInterpolator()

        val animLeft = ObjectAnimator.ofFloat(bu_left, "x", -150f).apply {
            interpolator = DecelerateInterpolator()
            doOnEnd { bu1.x -= BUTTONS_TRANSLATION_X }
        }

        val animSet = AnimatorSet()
        animSet.playTogether(animLeft)//, animLeft)
        animSet.duration = BUTTONS_TRANSLATION_DURATION

        buttonLeft.animate().xBy(addMenuOpen * BUTTONS_TRANSLATION_X).apply {
            duration = BUTTONS_TRANSLATION_DURATION
            interpolator = DecelerateInterpolator()
        }

        buttonRight.animate().xBy(-addMenuOpen * BUTTONS_TRANSLATION_X).apply {
            duration = BUTTONS_TRANSLATION_DURATION
            interpolator = DecelerateInterpolator()
        }

        /*if (switchMenuOpen) {
            wipeOutMenu(0f, menu)
            switchMenuOpen = false
        }
         */

        if (addMenuOpen == -1) {
            addMenuOpen = 1
            c.connect(ll_add_menu, ConstraintSet.TOP, iv_icon, ConstraintSet.BOTTOM)
            animator.start()
        } else {
            addMenuOpen = -1
            c.connect(ll_add_menu, ConstraintSet.TOP, main_layout, ConstraintSet.BOTTOM)
            animator.setFloatValues(0f)
            animator.start()
        }
        TransitionManager.beginDelayedTransition(findViewById<ConstraintLayout>(main_layout), transition)
        c.applyTo(findViewById(main_layout)) //appearance
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