package com.example.app

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.SwipeHelpers.DeleteSwipe
import com.example.app.SwipeHelpers.SwipeParamsHolder
import com.example.app.adapters.CardsAdapter
import com.example.app.database.DatabaseCards
import com.example.app.database.DatabaseProfile
import com.example.app.models.CardModel
import com.example.app.models.ProfileModel
import kotlinx.android.synthetic.main.activity_cards.*
import kotlinx.android.synthetic.main.activity_cards.angle
import kotlinx.android.synthetic.main.activity_cards.angle_image
import kotlinx.android.synthetic.main.activity_cards.layout_menu
import kotlinx.android.synthetic.main.activity_cards.plus_image
import kotlinx.android.synthetic.main.activity_identity.*


class CardActivity : ButtonsFunctionality() {

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
                , plus_image, R.id.main_layout_cards, R.id.add_menu, false
            )
        }

        setupListOfDataIntoRecycleView()
        plus_image.setOnClickListener {

        }
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
                , plus_image, R.id.main_layout_cards, R.id.add_menu, false
            )
        }
    }

    fun deleteItem(card: CardModel) {
        val handler = DatabaseCards(this)
        if (handler.deleteCard(card) == -1) {
            Toast.makeText(this, "ErRorr", Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
        setupListOfDataIntoRecycleView()
    }

    private fun getCards(): ArrayList<CardModel> {
        return DatabaseCards(this).viewCards()
    }

    private fun setupListOfDataIntoRecycleView() {
        if (getCards().size > 0) {
            findViewById<RecyclerView>(R.id.cards_layout).visibility = View.VISIBLE
            findViewById<RecyclerView>(R.id.cards_layout).layoutManager = LinearLayoutManager(this)
            val itemAdapter =
                CardsAdapter(this, getCards())
            findViewById<RecyclerView>(R.id.cards_layout).adapter = itemAdapter
        }
        else {
            findViewById<RecyclerView>(R.id.cards_layout).visibility = View.GONE
        }
        //val d = DeleteSwipe(SwipeParamsHolder(rv_ca, supportFragmentManager))
        //ItemTouchHelper(d).attachToRecyclerView(rv_identities)
    }

}