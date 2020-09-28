package com.radiostudies.main.ui.fragment

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import com.radiostudies.main.common.fragment.BaseFragment
import com.radiostudies.main.common.util.getJsonDataFromAsset
import com.radiostudies.main.common.util.reObserve
import com.radiostudies.main.common.util.setEnable
import com.radiostudies.main.ui.fragment.databinding.InitialQuestionsFragmentBinding
import com.radiostudies.main.ui.model.ScreenQuestionListModel
import com.radiostudies.main.ui.model.ScreenQuestionModel
import com.radiostudies.main.ui.model.ScreenQuestionState
import com.radiostudies.main.ui.viewmodel.InitialQuestionsViewModel
import kotlinx.android.synthetic.main.initial_questions_fragment.*
import javax.inject.Inject

class InitialQuestionsFragment :
    BaseFragment<InitialQuestionsFragmentBinding, InitialQuestionsViewModel>() {

    private var listener: InitialQuestionsFragmentListener? = null

    @Inject
    override lateinit var viewModel: InitialQuestionsViewModel

    override fun createLayout() = R.layout.initial_questions_fragment

    override fun getBindingVariable() = BR.initialViewModel

    override fun initViews() {

        prev_btn.setOnClickListener {
            viewModel.updatePrevQuestion()
        }

        next_btn.setOnClickListener {
            viewModel.updateNextQuestion()
        }
    }

    override fun subscribeUi() {
        with(viewModel) {
            reObserve(getScreenLiveData(), ::onScreenStateChanged)
        }
    }

    private fun onScreenStateChanged(state: ScreenQuestionState?) {
        when (state) {
            is ScreenQuestionModel -> viewModel.parseScreenQuestion(context?.let { state.fileName?.let { fileName ->
                getJsonDataFromAsset(it,
                    fileName
                )
            } })

            is ScreenQuestionListModel -> {
                question_number.text = "Question No. ${state.screenQuestion?.number}"
                question_label.text = state.screenQuestion?.question
                prev_btn.setEnable(state.screenQuestion?.number != 1)
                next_btn.setEnable(state.screenQuestion?.number != 5)

                if (state.screenQuestion?.isSingleAnswer == true) {
                    state.screenQuestion.options?.let {loadSingleOption(it)}
                } else {
                    state.screenQuestion?.options?.let { loadMultipleOptions(it) }
                }
            }
        }
    }

    private fun loadMultipleOptions(list: List<String>) {
        selection_layout.apply {
            removeAllViews()
            invalidate()
            for (i in list.indices) {
                // create a radio button
                val cb = CheckBox(context)
                // set text for the radio button
                cb.text = list[i]
                // assign an automatically generated id to the radio button
                cb.id = View.generateViewId()
                // add radio button to the radio group
                addView(cb)
            }
        }
    }

    private fun loadSingleOption(list: List<String>) {
        selection_layout.apply {
            removeAllViews()
            invalidate()
            var radioGroup = RadioGroup(context)
            radioGroup.orientation = RadioGroup.VERTICAL
            for (i in list.indices) {
                val rb = RadioButton(context)
                // set text for the radio button
                rb.text = list[i]
                // assign an automatically generated id to the radio button
                rb.id = View.generateViewId()
                // add radio button to the radio group
                radioGroup.addView(rb)
            }

            addView(radioGroup)
        }
    }

    private fun enableDisableBtn(btn: Button, enable: Boolean) {
        btn.isEnabled = enable
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is InitialQuestionsFragmentListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface InitialQuestionsFragmentListener {
        fun navigateToMainInfo(view: View?)
    }

    companion object {
        fun newInstance() = InitialQuestionsFragment()
    }
}