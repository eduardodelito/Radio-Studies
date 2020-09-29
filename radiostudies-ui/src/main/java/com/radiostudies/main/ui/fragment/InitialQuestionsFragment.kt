package com.radiostudies.main.ui.fragment

import android.content.Context
import android.view.View
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
            with(viewModel) {
                updateQuestion(loadOptions())
                updateNextQuestion()
                next_btn.setEnable(false)
                if (this.index == 5) {
                    this.index = 4
                    listener?.navigateToMainInfo(it)
                }
            }
        }
        next_btn.setEnable(false)
    }

    override fun subscribeUi() {
        with(viewModel) {
            reObserve(getScreenLiveData(), ::onScreenStateChanged)
        }
    }

    private fun onScreenStateChanged(state: ScreenQuestionState?) {
        when (state) {
            is ScreenQuestionModel -> viewModel.parseScreenQuestion(context?.let {
                state.fileName?.let { fileName ->
                    getJsonDataFromAsset(
                        it,
                        fileName
                    )
                }
            })

            is ScreenQuestionListModel -> {
                var screenQuestion = state.screenQuestion
                viewModel.currentQuestion = screenQuestion?.question

                question_number.text = "Question No. ${screenQuestion?.number}"
                question_label.text = screenQuestion?.question
                prev_btn.setEnable(screenQuestion?.number != 1)

                if (screenQuestion?.isSingleAnswer == true) {
                    screenQuestion.options?.let { loadSingleOption(it) }
                } else {
                    screenQuestion?.options?.let { loadMultipleOptions(it) }
                }

            }
        }
    }

    private fun loadOptions(): MutableList<String> {
        var selectedOptions = mutableListOf<String>()
        if (selection_layout.tag != null) {
            when (selection_layout.tag.toString().toInt()) {
                0 -> {
                    selectedOptions = getSelectedSingleAnswer()
                }

                1 -> {
                    selectedOptions = getSelectedMultipleAnswer()
                }
            }
        }
        return selectedOptions
    }

    private fun loadMultipleOptions(list: List<String>) {
        selection_layout.apply {
            tag = 1
            removeAllViews()
            invalidate()
            for (i in list.indices) {
                val cb = CheckBox(context)
                cb.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked && buttonView.text.contains("NONE")) {
                        uncheckCheckBoxes(true)
                    } else if (isChecked && !buttonView.text.contains("NONE")) {
                        uncheckCheckBoxes(false)
                    }
                    next_btn.setEnable(true)
                }
                // set text for the radio button
                cb.text = list[i]
                // assign an automatically generated id to the radio button
                cb.id = View.generateViewId()
                // add radio button to the radio group
                addView(cb)
            }
        }
    }

    private fun getSelectedMultipleAnswer(): MutableList<String> {
        val list = mutableListOf<String>()
        selection_layout.apply {
            val count: Int = this.childCount
            for (i in 0 until count) {
                val cb = getChildAt(i) as CheckBox

                if (cb.isChecked) {
                    list.add(cb.text.toString())
                }
            }
        }
        return list
    }

    private fun uncheckCheckBoxes(isNoneCheckBox: Boolean) {
        selection_layout.apply {
            val count: Int = this.childCount
            for (i in 0 until count) {
                val cb = getChildAt(i) as CheckBox
                if (isNoneCheckBox) {
                    if (!cb.text.contains("NONE")) {
                        cb.isChecked = false
                    }
                } else {
                    if (cb.text.contains("NONE")) {
                        cb.isChecked = false
                    }
                }
            }
        }
    }

    private fun loadSingleOption(list: List<String>) {
        selection_layout.apply {
            tag = 0
            removeAllViews()
            invalidate()
            var radioGroup = RadioGroup(context)
            radioGroup.orientation = RadioGroup.VERTICAL
            for (i in list.indices) {
                val rb = RadioButton(context)
                rb.setOnClickListener {
                    next_btn.setEnable(true)
                }
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

    private fun getSelectedSingleAnswer(): MutableList<String> {
        val list = mutableListOf<String>()
        selection_layout.apply {
            val rg = getChildAt(0) as RadioGroup
            val selected = findViewById<RadioButton>(rg.checkedRadioButtonId)
            list.add(selected.text.toString())
        }
        return list
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