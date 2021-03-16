package com.example.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bu = findViewById<TextView>(R.id.bu)
        bu.setOnClickListener {
            val aniRotate = AnimationUtils.loadAnimation(applicationContext,R.anim.rotate)
            bu.startAnimation(aniRotate)
            val text = findViewById<TextView>(R.id.text)
            text.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.appearance))
            text.alpha = 0F
        }
        val Valera = "Krasava"
        val Serega = "Bol'noj"
    }
}