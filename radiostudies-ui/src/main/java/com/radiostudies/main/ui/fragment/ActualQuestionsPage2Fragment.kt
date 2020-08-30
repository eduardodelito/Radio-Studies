package com.radiostudies.main.ui.fragment

import com.radiostudies.main.common.fragment.BaseFragment
import com.radiostudies.main.ui.fragment.databinding.ActualQuestionsPage2FragmentBinding
import com.radiostudies.main.ui.viewmodel.ActualQuestionsPage2ViewModel
import javax.inject.Inject

class ActualQuestionsPage2Fragment : BaseFragment<ActualQuestionsPage2FragmentBinding, ActualQuestionsPage2ViewModel>() {

    @Inject
    override lateinit var viewModel: ActualQuestionsPage2ViewModel

    override fun createLayout() = R.layout.actual_questions_page2_fragment

    override fun getBindingVariable() = BR.actualQuestionsPage2ViewModel

    override fun initViews() {

    }

    override fun subscribeUi() {

    }

    companion object {
        fun newInstance() = ActualQuestionsPage2Fragment()
    }
}
