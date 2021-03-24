package com.example.app

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.adapters.ItemAdapter
import com.example.app.database.DatabaseCards
import com.example.app.models.CardModel
import kotlinx.android.synthetic.main.activity_cards.*


class Cards : AppCompatActivity() {
    var addMenuOpen = true
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cards)
        val bu = findViewById<ImageButton>(R.id.bu1)
        bu.setOnClickListener {
            val aniRotate = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate)
            bu.startAnimation(aniRotate)
        }
        plus_button.setOnClickListener {
            plusButton(this.requireViewById(plus_button.id))
        }
        var visible = false
        var alfha: Float

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

        setupListOfDataIntoRecycleView()
    }

    fun plusButton(view: View) {
        val bu = findViewById<ImageView>(R.id.plus_image)
        val animator = ObjectAnimator.ofFloat(bu, View.ROTATION, 135f)
        animator.duration = 300;
        animator.addListener(object: AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled = false;
            }
            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled = true;

            }
        })//вращение

        val mainLayout = findViewById<ConstraintLayout>(R.id.main_layout)
        val c = ConstraintSet()
        c.clone(mainLayout)
        val transition = AutoTransition()
        transition.duration = 300
        transition.interpolator = AccelerateDecelerateInterpolator()
        if (addMenuOpen) {
            addMenuOpen = false
            c.connect(R.id.add_menu, ConstraintSet.TOP, R.id.card_big, ConstraintSet.BOTTOM)
            animator.start()
        }
        else {
            addMenuOpen = true
            c.connect(R.id.add_menu, ConstraintSet.TOP, R.id.main_layout, ConstraintSet.BOTTOM)
            animator.setFloatValues(0f)
            animator.start()
        }
        TransitionManager.beginDelayedTransition(mainLayout, transition)
        c.applyTo(mainLayout) //появление
    }

    fun addCard(view: View) {
        val number = findViewById<EditText>(R.id.et_number).text.toString()
        val holder = findViewById<EditText>(R.id.et_holder).text.toString()
        val date = findViewById<EditText>(R.id.et_expiry).text.toString()
        val cvc = findViewById<EditText>(R.id.et_cvc).text.toString().toIntOrNull()
        val pin = findViewById<EditText>(R.id.et_pin).text.toString().toIntOrNull()
        val comment = findViewById<EditText>(R.id.et_comment).text.toString()
        val cardsHandler: DatabaseCards = DatabaseCards(this)
        val status = cardsHandler.addCard(CardModel(0, number, holder, date, cvc!!, pin!!, comment))
        if (status > -1) {
            Toast.makeText(this, "Card added", Toast.LENGTH_SHORT).show()
            findViewById<EditText>(R.id.et_number).text.clear()
            findViewById<EditText>(R.id.et_holder).text.clear()
            findViewById<EditText>(R.id.et_expiry).text.clear()
            findViewById<EditText>(R.id.et_cvc).text.clear()
            findViewById<EditText>(R.id.et_pin).text.clear()
            findViewById<EditText>(R.id.et_comment).text.clear()
            setupListOfDataIntoRecycleView()
            plusButton(view)
        }
    }

    private fun getCards(): ArrayList<CardModel> {
        return DatabaseCards(this).viewCards()
    }

    private fun setupListOfDataIntoRecycleView() {
        if (getCards().size > 0) {
            findViewById<RecyclerView>(R.id.cards_layout).visibility = View.VISIBLE
            findViewById<RecyclerView>(R.id.cards_layout).layoutManager = LinearLayoutManager(this)
            val itemAdapter =
                ItemAdapter(this, getCards())
            findViewById<RecyclerView>(R.id.cards_layout).adapter = itemAdapter
        }
        else {
            findViewById<RecyclerView>(R.id.cards_layout).visibility = View.GONE
        }
    }

    //switchers
    fun toCards(view: View) {
        val intent = Intent(this, Cards::class.java)
        startActivity(intent)
        finish()
    }

    fun toAccounts(view: View) {

    }

    fun toIdent(view: View) {

    }

    fun toNotes(view: View) {
        val intent = Intent(this, NotesActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun test(view: View) {
        Toast.makeText(this, "HAHA", Toast.LENGTH_SHORT).show()
    }
}