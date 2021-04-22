package com.example.app

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.SwipeHelpers.DeleteSwipe
import com.example.app.SwipeHelpers.ProfileSwipeHelper
import com.example.app.SwipeHelpers.SwipeParamsHolder
import com.example.app.adapters.CardsAdapter
import com.example.app.adapters.ProfilesAdapter
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
import kotlinx.android.synthetic.main.activity_profile.*


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
            //TODO manage plus button if update form is opened
            plusButton(
                this.findViewById(R.id.plus_image), R.id.card_big
                , plus_image, R.id.main_layout_cards, R.id.add_menu, false
            )
        }

        setupListOfDataIntoRecycleView()
    }

    var updateFormOpened = false
    var currentItem : CardsAdapter.ViewHolder? = null
    fun addCard(view: View) {

        val number = findViewById<EditText>(R.id.et_number).text.toString()
        val holder = findViewById<EditText>(R.id.et_holder).text.toString()
        val date = findViewById<EditText>(R.id.et_expiry).text.toString()
        val cvc = findViewById<EditText>(R.id.et_cvc).text.toString().toIntOrNull()
        val pin = findViewById<EditText>(R.id.et_pin).text.toString().toIntOrNull()
        val comment = findViewById<EditText>(R.id.et_comment).text.toString()
        val cardsHandler = DatabaseCards(this)

        if (!updateFormOpened) {
            val status =
                cardsHandler.addCard(CardModel(0, number, holder, date, cvc!!, pin!!, comment))
            if (status > -1) {
                Toast.makeText(this, "Card added", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            if (currentItem != null) {
                (cards_layout.adapter as CardsAdapter)
                    .updateCard(
                        currentItem!!,
                        CardModel(-1, number, holder, date, cvc!!, pin!!, comment)
                    )
            }
            updateFormOpened = false
        }
        findViewById<EditText>(R.id.et_number).text.clear()
        findViewById<EditText>(R.id.et_holder).text.clear()
        findViewById<EditText>(R.id.et_expiry).text.clear()
        findViewById<EditText>(R.id.et_cvc).text.clear()
        findViewById<EditText>(R.id.et_pin).text.clear()
        findViewById<EditText>(R.id.et_comment).text.clear()
        setupListOfDataIntoRecycleView()
        plusButton(
            this.findViewById(R.id.plus_image), R.id.card_big
            , plus_image, R.id.main_layout_cards, R.id.add_menu, false)
    }

    //TODO get rid of this method!
    fun deleteItem(card: CardModel) {
        val handler = DatabaseCards(this)
        if (handler.deleteCard(card) == -1) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
        setupListOfDataIntoRecycleView()
    }

    private fun getCards(): ArrayList<CardModel> {
        return DatabaseCards(this).viewCards()
    }

    fun setupListOfDataIntoRecycleView() {
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
        val d = DeleteSwipe(SwipeParamsHolder(cards_layout, supportFragmentManager))
        ItemTouchHelper(d).attachToRecyclerView(cards_layout)

        val deleteSwipeHelperRight = object : ProfileSwipeHelper(ItemTouchHelper.RIGHT) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                plusButton(
                    findViewById(R.id.plus_image), R.id.card_big,
                    plus_image, R.id.main_layout_cards, R.id.add_menu, false
                )
                updateFormOpened = true
                currentItem = viewHolder as CardsAdapter.ViewHolder
                et_number.setText((viewHolder).number.text)
                et_holder.setText(viewHolder.holder.text)
                et_expiry.setText(viewHolder.expiry.text)
                et_cvc.setText(viewHolder.cvc.text)
                et_pin.setText(viewHolder.pin.text)
                et_comment.setText(viewHolder.comment.text)
                //add_button_profile.setText(R.string.update)
                //TODO change button text correctly
            }
        }
        ItemTouchHelper(deleteSwipeHelperRight).attachToRecyclerView(cards_layout)
    }

}