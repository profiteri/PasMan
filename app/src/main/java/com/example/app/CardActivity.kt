package com.example.app

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.addListener
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.adapters.CardsAdapter
import com.example.app.crypto.Encrypter
import com.example.app.database.DatabaseCards
import com.example.app.models.CardModel
import com.example.app.swipeHelpers.*
import kotlinx.android.synthetic.main.activity_cards.*
import kotlinx.android.synthetic.main.activity_cards.angle
import kotlinx.android.synthetic.main.activity_cards.angle_image
import kotlinx.android.synthetic.main.activity_cards.layout_menu
import kotlinx.android.synthetic.main.activity_cards.plus_image


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
                R.id.card_big, plus_image,
                R.id.main_layout_cards, R.id.add_menu, false
            )
            if (updateFormOpened) {
                et_number.setText("")
                et_holder.setText("")
                et_expiry.setText("")
                et_cvc.setText("")
                et_pin.setText("")
                et_comment.setText("")
                currentItem?.foreground?.alpha = 1f
                editSwipeHelper.attachToRecyclerView(null)
                editSwipeHelper.attachToRecyclerView(cards_layout)
                //cards_layout.layoutManager = LinearLayoutManager(this)
                updateFormOpened = false
            }
            else add_button.setText(R.string.add)
        }
        setupListOfDataIntoRecycleView()
    }

    var updateFormOpened = false
    fun addCard(view: View) {
        val encrypter = Encrypter(null)
        val number = encrypter.encryptString(et_number.text.toString())
        val holder = Encrypter(encrypter.getIv()).encryptString(et_holder.text.toString())
        val date = Encrypter(encrypter.getIv()).encryptString(et_expiry.text.toString())
        val cvc = Encrypter(encrypter.getIv()).encryptString(et_cvc.text.toString())
        val pin = Encrypter(encrypter.getIv()).encryptString(et_pin.text.toString())
        val comment = Encrypter(encrypter.getIv()).encryptString(et_comment.text.toString())
        val cardsHandler = DatabaseCards(this)

        if (!updateFormOpened) {
            val status =
                cardsHandler.addCard(CardModel(0, number, holder, date, cvc, pin, comment, encrypter.getIv()))
            if (status > -1) {
                Toast.makeText(this, "Card added", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            if (currentItem != null) {
                (cards_layout.adapter as CardsAdapter)
                    .updateCard(
                        currentItem!!,
                        CardModel(-1, number, holder, date, cvc, pin, comment, encrypter.getIv())
                    )
            }
            updateFormOpened = false
        }
        et_number.text?.clear()
        et_holder.text?.clear()
        et_expiry.text?.clear()
        et_cvc.text?.clear()
        et_pin.text?.clear()
        et_comment.text?.clear()
        setupListOfDataIntoRecycleView()
        plusButton(
            R.id.card_big, plus_image,
            R.id.main_layout_cards, R.id.add_menu, false)
    }


    private fun getCards(): ArrayList<CardModel> {
        return DatabaseCards(this).viewCards()
    }

    var currentItem : CardsAdapter.ViewHolder? = null

    private lateinit var deleteSwipeHelper : ItemTouchHelper
    private lateinit var editSwipeHelper : ItemTouchHelper

    fun setupListOfDataIntoRecycleView() {
        if (getCards().size > 0) {
            cards_layout.visibility = View.VISIBLE
            cards_layout.layoutManager = LinearLayoutManager(this)
            cards_layout.adapter = CardsAdapter(this, getCards())
        }
        else {
            cards_layout.visibility = View.GONE
        }


        /*val d = object : SmartDeleteSwipe(SwipeParamsHolder(cards_layout, supportFragmentManager)) {
            override fun reattachHelper() {
                deleteSwipeHelper.attachToRecyclerView(null)
                deleteSwipeHelper.attachToRecyclerView(cards_layout)
            }
        }

         */

        val d = object : ProfileSwipeHelper(ItemTouchHelper.LEFT) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                DeleteItem(viewHolder, cards_layout, deleteSwipeHelper).show(supportFragmentManager, "delete_dialog")
            }
        }
        deleteSwipeHelper = ItemTouchHelper(d)
        deleteSwipeHelper.attachToRecyclerView(cards_layout)
        /*val d = DeleteSwipe(SwipeParamsHolder(cards_layout, supportFragmentManager))
        deleteSwipeHepler = ItemTouchHelper(d)
        deleteSwipeHepler.attachToRecyclerView(cards_layout)

         */

        val deleteSwipeHelperRight = object : ProfileSwipeHelper(ItemTouchHelper.RIGHT) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                plusButton(
                    R.id.card_big,
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
                add_button.setText(R.string.update)
            }
        }
        editSwipeHelper = ItemTouchHelper(deleteSwipeHelperRight)
        editSwipeHelper.attachToRecyclerView(cards_layout)

    }

    var front = true

    fun flipCard(view: View) {

        val mainLayout : FrameLayout = view.parent.parent as FrameLayout
        val backside : ConstraintLayout = mainLayout.findViewById(R.id.card_backside)
        val foreground : ConstraintLayout = mainLayout.findViewById(R.id.card_foreground)
        val aniSet = AnimatorSet()
        val translation: ObjectAnimator
        val appearance: ObjectAnimator
        val disappearance: ObjectAnimator
        if (front) {
            translation = ObjectAnimator.ofFloat(
                mainLayout,
                View.ROTATION_Y,
                0f,
                180f
            )
            appearance = ObjectAnimator.ofFloat(
                backside,
                View.ALPHA,
                0f, 1f
            )
            disappearance = ObjectAnimator.ofFloat(
                foreground,
                View.ALPHA,
                1f, 0f
            )
            front = false
        } else {
            translation = ObjectAnimator.ofFloat(
                mainLayout,
                View.ROTATION_Y,
                -180f,
                0f
            )
            appearance = ObjectAnimator.ofFloat(
                foreground,
                View.ALPHA,
                0f, 1f
            )
            disappearance = ObjectAnimator.ofFloat(
                backside,
                View.ALPHA,
                1f, 0f
            )
            front = true
        }
        aniSet.playTogether(translation, appearance, disappearance)
        aniSet.duration = 300
        /*aniSet.addListener(
            onEnd = {
                cardTouchHelper.attachToRecyclerView(null)
                cardTouchHelper.attachToRecyclerView(cards_layout)
            }
        )

         */
        aniSet.start()

        //recyclerView.layoutManager = LinearLayoutManager(context)
        //cards_layout.adapter?.notifyItemChanged(viewHolder.adapterPosition)
        //ItemTouchHelper(this).startSwipe(viewHolder)
    }
}