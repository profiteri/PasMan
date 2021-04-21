package com.example.app.activity_main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.transition.addListener
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import com.example.app.R
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AesGcmKeyManager
import com.google.crypto.tink.config.TinkConfig
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.fragment_registration.view.*
import java.security.Key
import java.security.KeyStore
import javax.crypto.KeyGenerator

private var ARG_PARAM1 : KeyStore? = null

class RegistrationFragment : Fragment() {

    var keyStore : KeyStore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            keyStore = ARG_PARAM1
        }
    }

    @Synchronized
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registration, container, false)

        val transition = AutoTransition()
        val listener = ViewTreeObserver.OnWindowFocusChangeListener { _ ->
            val c = ConstraintSet()
            c.clone(view.main_layout_welcome)
            transition.duration = 1000
            transition.interpolator = DecelerateInterpolator(2f)
            c.connect(
                R.id.tv_welcome,
                ConstraintSet.TOP,
                R.id.main_layout_welcome,
                ConstraintSet.TOP
            )
            c.clear(R.id.tv_welcome, ConstraintSet.BOTTOM)
            TransitionManager.beginDelayedTransition(view.main_layout_welcome, transition)
            c.applyTo(view.main_layout_welcome)
        }
        view?.viewTreeObserver?.addOnWindowFocusChangeListener(listener)
        transition.addListener(
            onEnd = {
                view.ll_password.x = -1000f
                view.ll_password.visibility = View.VISIBLE
                view.ll_password
                    .animate()
                    .translationX(0f)
                    .setDuration(700)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            view.btn_create_password.visibility = View.VISIBLE
                            val a = ObjectAnimator
                                .ofFloat(view.btn_create_password, "alpha", 0f, 1f)
                                .setDuration(300)
                            a.startDelay = 200
                            a.start()
                        }
                    })
                    .start()
                view?.viewTreeObserver?.removeOnWindowFocusChangeListener(listener)
            }
        )

        view.btn_create_password.setOnClickListener {
            val paramsBuilder = KeyGenParameterSpec.Builder(
                et_create_password.text.toString(),
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
            paramsBuilder.apply {
                setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                setRandomizedEncryptionRequired(false)
            }
            val keyGenParams = paramsBuilder.build()
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                "AndroidKeyStore")
            keyGenerator.init(keyGenParams)
            keyGenerator.generateKey()
            //(activity as MainActivity).alias = et_create_password.text.toString()
            (activity as MainActivity).cl_navigation.visibility = View.VISIBLE
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .remove(this)
                //.add(R.id.ll_fragment_registration, LoginFragment.newInstance())
                .commit()
        }
        return view
    }


    companion object {
        @JvmStatic
        fun newInstance(keyStore: KeyStore) =
            RegistrationFragment().apply {
                arguments = Bundle().apply {
                    ARG_PARAM1 = keyStore
                }
            }
    }
}