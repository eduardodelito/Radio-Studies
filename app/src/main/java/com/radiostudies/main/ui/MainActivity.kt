package com.radiostudies.main.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import com.radiostudies.main.ui.fragment.*
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity(), LoginFragment.LoginFragmentListener,
    InitialQuestionsFragment.InitialQuestionsFragmentListener, MainInfoFragment.MainInfoFragmentListener,
    ActualQuestionsFragment.ActualQuestionsFragmentListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun navigateToInitialScreen(view: View?) {
        val action = LoginFragmentDirections.actionLoginFragmentToInitialQuestionsFragment()
        view?.findNavController()?.navigate(action)
    }

    override fun navigateToMainInfo(view: View?) {
        val action = InitialQuestionsFragmentDirections.actionInitialQuestionsFragmentToMainInfoFragment()
        view?.findNavController()?.navigate(action)
    }

    override fun navigateToActualQuestions(view: View?) {
        //Do nothing
    }

    override fun navigateToActualQuestionsPage2(view: View?) {

    }
}
