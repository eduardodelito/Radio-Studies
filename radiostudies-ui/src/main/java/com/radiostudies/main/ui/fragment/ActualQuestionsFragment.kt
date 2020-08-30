package com.radiostudies.main.ui.fragment

import android.content.Context
import android.view.View
import com.radiostudies.main.common.fragment.BaseFragment
import com.radiostudies.main.ui.fragment.databinding.ActualQuestionsFragmentBinding
import com.radiostudies.main.ui.viewmodel.ActualQuestionsViewModel
import kotlinx.android.synthetic.main.actual_questions_fragment.*
import javax.inject.Inject

class ActualQuestionsFragment : BaseFragment<ActualQuestionsFragmentBinding, ActualQuestionsViewModel>() {

    private var listener: ActualQuestionsFragmentListener? = null

    @Inject
    override lateinit var viewModel: ActualQuestionsViewModel

    override fun createLayout() = R.layout.actual_questions_fragment

    override fun getBindingVariable() = BR.actualQuestionsViewModel

    override fun initViews() {
        continue_button.setOnClickListener {
            listener?.navigateToActualQuestionsPage2(it)
        }
    }

    override fun subscribeUi() {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActualQuestionsFragmentListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface ActualQuestionsFragmentListener {
        fun navigateToActualQuestionsPage2(view: View?)
    }
}