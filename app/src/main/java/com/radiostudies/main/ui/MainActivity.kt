package com.radiostudies.main.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import com.radiostudies.main.ui.fragment.*
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity(), LoginFragment.LoginFragmentListener,
    InitialQuestionsFragment.InitialQuestionsFragmentListener, MainInfoFragment.MainInfoFragmentListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun navigateToInitialScreen(view: View?) {
        val action = LoginFragmentDirections.actionLoginFragmentToInitialQuestionsFragment()
        view?.findNavController()?.navigate(action)
    }

    override fun navigateBack() {
        findNavController(R.id.navHostFragment).navigateUp()
    }

    override fun navigateToDiaryScreen(view: View?) {
        val action = LoginFragmentDirections.actionLoginFragmentToDiaryFragment()
        view?.findNavController()?.navigate(action)
    }

    override fun navigateToMainInfo(view: View?) {
        val action = InitialQuestionsFragmentDirections.actionInitialQuestionsFragmentToMainInfoFragment()
        view?.findNavController()?.navigate(action)
    }

    override fun navigateToActualQuestions(view: View?) {
        val action = MainInfoFragmentDirections.actionMainInfoFragmentToActualQuestionsFragment()
        view?.findNavController()?.navigate(action)
    }
}
