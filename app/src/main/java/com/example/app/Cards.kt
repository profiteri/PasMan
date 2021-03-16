package com.example.app

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class Cards : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cards)

        val bu = findViewById<ImageButton>(R.id.bu1)
        bu.setOnClickListener {
            val aniRotate = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate)
            bu.startAnimation(aniRotate)
            //val text = findViewById<TextView>(R.id.text)
            //text.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.appearance))
            //text.alpha = 0
        }

        val angle = findViewById<Button>(R.id.angle)
        angle.setOnClickListener {
            /*val aniMove = AnimationUtils.loadAnimation(applicationContext, R.anim.move)
            val image = findViewById<ImageView>(R.id.angle_image)
            val aniBack = AnimationUtils.loadAnimation(applicationContext, R.anim.move_back)
            image.startAnimation(aniMove)*/
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
            val layout = findViewById<LinearLayout>(R.id.layout_menu)
            animator.start()
            layout.visibility = View.VISIBLE

            val anim = ValueAnimator.ofInt(0, 400)
            anim.addUpdateListener { valueAnimator ->
                val a = valueAnimator.animatedValue as Int
                val layoutParams = layout.layoutParams
                layoutParams.height = a
                layout.layoutParams = layoutParams
            }
            anim.start()
        }

    }
    fun test(view: View) {
        Toast.makeText(this, "HAHA", Toast.LENGTH_SHORT).show()
    }
}