package com.radiostudies.main.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import com.radiostudies.main.ui.fragment.InitialQuestionsFragment
import com.radiostudies.main.ui.fragment.InitialQuestionsFragmentDirections
import com.radiostudies.main.ui.fragment.LoginFragment
import com.radiostudies.main.ui.fragment.LoginFragmentDirections
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity(), LoginFragment.LoginFragmentListener,
    InitialQuestionsFragment.InitialQuestionsFragmentListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun navigateToMainScreen(view: View?) {
        val action = LoginFragmentDirections.actionLoginFragmentToInitialQuestionsFragment()
        view?.findNavController()?.navigate(action)
    }

    override fun navigateToMainInfo(view: View?) {
        val action = InitialQuestionsFragmentDirections.actionInitialQuestionsFragmentToMainInfoFragment()
        view?.findNavController()?.navigate(action)
    }
}