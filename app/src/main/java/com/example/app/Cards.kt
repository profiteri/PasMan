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
import kotlinx.android.synthetic.main.activity_cards.angle
import kotlinx.android.synthetic.main.activity_cards.angle_image
import kotlinx.android.synthetic.main.activity_cards.layout_menu
import kotlinx.android.synthetic.main.activity_cards.plus_image
import kotlinx.android.synthetic.main.activity_profile.*


class Cards : ButtonsFunctionality() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cards)
        bu1.setOnClickListener {
            rotate(bu1)
        }

        angle.setOnClickListener {
            selectMenu(angle, angle_image, layout_menu)
        }

        plus_image.setOnClickListener {
            plusButton(
                this.findViewById(R.id.plus_image), R.id.card_big
                , plus_image, R.id.main_layout_cards, R.id.add_menu
            )
        }

        setupListOfDataIntoRecycleView()
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
            plusButton(
                this.findViewById(R.id.plus_image), R.id.card_big
                , plus_image, R.id.main_layout_cards, R.id.add_menu
            )
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

    fun test(view: View) {
        Toast.makeText(this, "HAHA", Toast.LENGTH_SHORT).show()
    }
}