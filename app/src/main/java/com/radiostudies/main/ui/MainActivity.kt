package com.radiostudies.main.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.radiostudies.main.db.model.Diary
import com.radiostudies.main.ui.fragment.*
import com.radiostudies.main.ui.model.diary.DiaryModel
import com.radiostudies.main.ui.model.main.MainInfo
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity(), LoginFragment.LoginFragmentListener,
    InitialQuestionsFragment.InitialQuestionsFragmentListener,
    MainInfoFragment.MainInfoFragmentListener,
    ActualQuestionsFragment.ActualQuestionsFragmentListener,
    DiaryFragment.OnDiaryFragmentListener,
    DiaryDetailsFragment.DiaryDetailsFragmentListener,
    AddDiaryFragment.AddDiaryFragmentListener {
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

    override fun exit(message: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
        builder.setPositiveButton(getString(com.radiostudies.main.ui.fragment.R.string.yes_label)) { dialog, _ ->
            dialog.dismiss()
            findNavController(R.id.navHostFragment).navigateUp()
        }
        builder.setNegativeButton(getString(com.radiostudies.main.ui.fragment.R.string.no_label)) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun navigateToDiaryScreen(view: View?) {
        val action = LoginFragmentDirections.actionLoginFragmentToDiaryFragment()
        view?.findNavController()?.navigate(action)
    }

    override fun navigateToMainInfo(view: View?) {
        val action =
            InitialQuestionsFragmentDirections.actionInitialQuestionsFragmentToMainInfoFragment()
        view?.findNavController()?.navigate(action)
    }

    override fun navigateToActualQuestions(view: View?, mainInfo: MainInfo?) {
        val action = MainInfoFragmentDirections.actionMainInfoFragmentToActualQuestionsFragment()
        val bundle = bundleOf(MainInfoFragment.MAIN_INFO to mainInfo)
        view?.findNavController()?.navigate(action.actionId, bundle)
    }

    override fun navigateToDiaryDetails(view: View?, diaryModel: DiaryModel?) {
        val action = DiaryFragmentDirections.actionDiaryFragmentToDiaryDetailsFragment()
        val bundle = bundleOf(DiaryFragment.DIARY_ITEM to diaryModel)
        view?.findNavController()?.navigate(action.actionId, bundle)
    }

    override fun showAppBar(show: Boolean) {
        if (show)
            supportActionBar?.show()
        else
            supportActionBar?.hide()
    }

    override fun navigateToAddDiaryScreen(view: View, selectedArea: String?, diary: Diary) {
        val action = DiaryDetailsFragmentDirections.actionDiaryDetailsFragmentToAddDiaryFragment()
        val bundle = Bundle()
        bundle.putString(AddDiaryFragment.AREA, selectedArea)
        bundle.putSerializable(AddDiaryFragment.DIARY, diary)
        view.findNavController().navigate(action.actionId, bundle)
    }
}
