package com.example.app

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet


class Cards : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cards)

        val bu = findViewById<ImageButton>(R.id.bu1)
        bu.setOnClickListener {
            val aniRotate = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate)
            bu.startAnimation(aniRotate)
        }

        var visible = false
        var alfha: Float
        var addMenuOpen = false

        val angle = findViewById<Button>(R.id.angle)
        angle.setOnClickListener {

            val image = findViewById<ImageView>(R.id.angle_image)
            val animator = ObjectAnimator.ofFloat(image, View.TRANSLATION_Y, -10f)
            animator.repeatCount = 1
            animator.repeatMode = ObjectAnimator.REVERSE
            animator.duration = 100;
            animator.addListener(object: AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    angle.isEnabled = false;
                }
                override fun onAnimationEnd(animation: Animator?) {
                    angle.isEnabled = true;
                }
            })
            animator.start()

            val layout = findViewById<LinearLayout>(R.id.layout_menu)

            if (!visible) {
                alfha = 1f
                visible = true
            }
            else {
                alfha = 0f
                visible = false
            }
            val anim = ObjectAnimator.ofFloat(layout, View.ALPHA, alfha)
            anim.addListener(object: AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    layout.alpha = alfha
                }
            })
            anim.duration = 50
            anim.start()
        }

        val addButton = findViewById<Button>(R.id.plus_button)
        val mainLayout = findViewById<ConstraintLayout>(R.id.main_layout)

        addButton.setOnClickListener {
            val c = ConstraintSet()
            c.clone(mainLayout)
            val transition = AutoTransition()
            transition.duration = 300
            transition.interpolator = AccelerateDecelerateInterpolator()
            if (addMenuOpen) {
                addMenuOpen = false
                c.connect(R.id.add_menu, ConstraintSet.TOP, R.id.card_big, ConstraintSet.BOTTOM)
            }
            else {
                addMenuOpen = true
                c.connect(R.id.add_menu, ConstraintSet.TOP, R.id.main_layout, ConstraintSet.BOTTOM)
            }
            TransitionManager.beginDelayedTransition(mainLayout, transition)
            c.applyTo(mainLayout)
        }
    }
    fun test(view: View) {
        Toast.makeText(this, "HAHA", Toast.LENGTH_SHORT).show()
    }
}