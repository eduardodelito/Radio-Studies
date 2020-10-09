package com.radiostudies.main.ui.fragment

import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.TextViewCompat
import com.radiostudies.main.common.fragment.BaseFragment
import com.radiostudies.main.common.util.*
import com.radiostudies.main.db.entity.Option
import com.radiostudies.main.ui.fragment.databinding.ActualQuestionsFragmentBinding
import com.radiostudies.main.ui.model.actual.*
import com.radiostudies.main.ui.viewmodel.ActualQuestionsViewModel
import kotlinx.android.synthetic.main.actual_questions_fragment.*
import java.util.*
import javax.inject.Inject


class ActualQuestionsFragment :
    BaseFragment<ActualQuestionsFragmentBinding, ActualQuestionsViewModel>() {

    @Inject
    override lateinit var viewModel: ActualQuestionsViewModel

    override fun createLayout() = R.layout.actual_questions_fragment

    override fun getBindingVariable() = BR.actualQuestionsViewModel

    override fun initViews() {
        activity?.actionBar?.title = getString(R.string.actual_question_title)
        actual_prev_btn.setOnClickListener {
            viewModel.queryActualQuestion(viewModel.minus())
            actual_selection_layout.removeAllViews()
        }
        actual_next_btn.setOnClickListener {
            viewModel.queryActualQuestion(viewModel.plus())
            viewModel.saveQuestion(
                DataQuestion(
                    actual_question_number.text.toString(),
                    getQuestionLabelText(),
                    actual_question_label.text.toString(),
                    viewModel.selectedOptions
                )
            )
            actual_selection_layout.removeAllViews()
        }

        load_options_label.setOnClickListener {
            dialogOptions(viewModel.currentOptions, viewModel.isSingleAnswer)
        }

        manual_input.afterTextChanged {
            if (it.isNotEmpty()) {
                actual_next_btn.setEnable(true)
            } else {
                actual_next_btn.setEnable(false)
            }
        }

        btn_save.setOnClickListener {
            dialogComplete()
        }
    }

    private fun getQuestionLabelText() =
        if (actual_question_header_label.isShown) actual_question_header_label.text.toString() else ""

    override fun subscribeUi() {
        with(viewModel) {
            reObserve(getActualLiveData(), ::onActualQuestionStateChanged)
        }
    }

    private fun onActualQuestionStateChanged(state: ActualQuestionState?) {
        when (state) {
            is AreaForm -> {
                viewModel.parseArea(state.fileName?.let {
                    getJsonDataFromAsset(
                        requireContext(),
                        it
                    )
                })
            }

            is ActualQuestionForm -> {
                viewModel.parseActualQuestions(state.fileName?.let {
                    getJsonDataFromAsset(
                        requireContext(),
                        it
                    )
                })
            }

            is ActualQuestionModel -> {
                var actualQuestion = state.actualQuestion
                actual_question_number.text = actualQuestion.code
                actual_question_header_label.setViewVisibility(actualQuestion.header)
                actual_question_label.text = actualQuestion.question
                actual_prev_btn.setEnable(state.isPrevEnable)
                actual_next_btn.setEnable(state.isNextEnable)
                btn_save.visibility = View.GONE

                if (actualQuestion.options[0].option == AREA) {
                    viewModel.loadAreas()
                } else {
                    viewModel.currentOptions = actualQuestion.options
                }

                actualQuestion.type?.let {
                    viewModel.isSingleAnswer = it == SINGLE_ANSWER
                    load_options_label.text = String.format(
                        getString(R.string.load_options_label),
                        it
                    )
                }
                if (actualQuestion.isManualInput) {
                    line3.visibility = View.GONE
                    load_options_label.visibility = View.GONE
                    manual_input.visibility = View.VISIBLE
                    manual_input.setText("")
                } else {
                    line3.visibility = View.VISIBLE
                    load_options_label.visibility = View.VISIBLE
                    manual_input.visibility = View.GONE
                }
            }
        }
    }

    private fun addOptions(selectedList: MutableList<Option>) {
        actual_selection_layout.apply {
            viewModel.selectedOptions.clear()
            removeAllViews()
            invalidate()
            for (i in selectedList.indices) {
                val option = selectedList[i]
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
                )
                val tv = AppCompatTextView(context)
                TextViewCompat.setTextAppearance(tv, R.style.AvenirHeavy_Black);
                tv.layoutParams = params
                tv.text = "[${i + 1}] ${selectedList[i].option}"
                addView(tv)
                if (option.option.contains(OTHER)) {
                    val editText = EditText(context)
                    editText.isSingleLine = false
                    editText.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
                    editText.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                    val maxLength = 60
                    val fArray = arrayOfNulls<InputFilter>(1)
                    fArray[0] = LengthFilter(maxLength)
                    editText.filters = fArray
                    editText.isVerticalScrollBarEnabled = true
                    editText.movementMethod = ScrollingMovementMethod.getInstance()
                    editText.scrollBarStyle = View.SCROLLBARS_INSIDE_INSET
                    editText.layoutParams = params
                    addView(editText)
                    viewModel.selectedOptions.add(Option("0", editText.text.toString()))
                } else {
                    viewModel.selectedOptions.add(Option(option.code, option.option))
                }
            }

            if (viewModel.isEndOfQuestion()) {
                btn_save.visibility = View.VISIBLE
                actual_next_btn.setEnable(false)
            } else {
                actual_next_btn.setEnable(true)
            }
        }
    }

    private fun dialogOptions(list: List<Option>, isSingleChoice: Boolean) {
        val listStr = mutableListOf<String>()
        list.forEach { listStr.add(it.option) }

        val listItems = listStr.toTypedArray()
        val checkedItems = BooleanArray(listItems.size)
        Arrays.fill(checkedItems, false)

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.choose_items)
        val selectedList = mutableListOf<Option>()
        if (isSingleChoice) {
            // add a list
            builder.setItems(listItems) { dialog, which ->
                val item = listItems[which]
                val code = list.find { option ->  option.option == item}?.code.toString()

                selectedList.add(Option(code, item))
                addOptions(selectedList)
                dialog.dismiss()
            }
        } else {
            // this will checked the items when user open the dialog
            builder.setMultiChoiceItems(listItems, checkedItems) { dialog, which, isChecked ->
                // Update the current focused item's checked status

                val dListView = (dialog as AlertDialog).listView
                when {
                    listItems[which].contains(NONE) -> {
                        for (i in listItems.indices) {
                            if (!listItems[i].contains(NONE)) {
                                dListView.setItemChecked(i, false)
                                checkedItems[i] = false
                            }
                        }
                    }
                    listItems[which].contains(OTHER) -> {
                        for (i in listItems.indices) {
                            if (!listItems[i].contains(OTHER)) {
                                dListView.setItemChecked(i, false)
                                checkedItems[i] = false
                            }
                        }
                    }
                    else -> {
                        for (i in listItems.indices) {
                            if (listItems[i].contains(NONE)) {
                                dListView.setItemChecked(i, false)
                                checkedItems[i] = false
                            } else if (listItems[i].contains(OTHER)) {
                                dListView.setItemChecked(i, false)
                                checkedItems[i] = false
                            }
                        }
                        checkedItems[which] = isChecked
                    }
                }
            }

            builder.setPositiveButton(DONE) { dialog, _ ->
                for (i in listItems.indices) {
                    val checked = checkedItems[i]
                    if (checked) {
                        val item = listItems[i]
                        val code = list.find { option ->  option.option == item}?.code.toString()
                        selectedList.add(Option(code, item))
                    }
                }
                addOptions(selectedList)
                dialog.dismiss()
            }
        }

        val dialog = builder.create()
        dialog.show()
    }


    private fun dialogComplete() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(R.string.terminate_msg)
        builder.setPositiveButton(getString(R.string.yes_label)) { dialog, _ ->
            dialog.dismiss()
        }
        builder.setNegativeButton(getString(R.string.no_label)) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    companion object {
        private const val SINGLE_ANSWER = "Single Answer"
        private const val DONE = "Done"
        private const val AREA = "Area"
        private const val OTHER = "Other"
        private const val NONE = "None"
        fun newInstance() = ActualQuestionsFragment()
    }
}