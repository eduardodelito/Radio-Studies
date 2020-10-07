package com.radiostudies.main.ui.fragment

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.TextViewCompat
import com.radiostudies.main.common.fragment.BaseFragment
import com.radiostudies.main.common.util.getJsonDataFromAsset
import com.radiostudies.main.common.util.reObserve
import com.radiostudies.main.common.util.setEnable
import com.radiostudies.main.common.util.setViewVisibility
import com.radiostudies.main.ui.fragment.databinding.ActualQuestionsFragmentBinding
import com.radiostudies.main.ui.model.actual.ActualQuestionForm
import com.radiostudies.main.ui.model.actual.ActualQuestionModel
import com.radiostudies.main.ui.model.actual.ActualQuestionState
import com.radiostudies.main.ui.model.actual.AreaForm
import com.radiostudies.main.ui.viewmodel.ActualQuestionsViewModel
import kotlinx.android.synthetic.main.actual_questions_fragment.*
import java.util.*
import javax.inject.Inject


class ActualQuestionsFragment :
    BaseFragment<ActualQuestionsFragmentBinding, ActualQuestionsViewModel>() {

    private var listener: ActualQuestionsFragmentListener? = null

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
            actual_selection_layout.removeAllViews()
        }

        load_options_label.setOnClickListener {
            dialogOptions(viewModel.currentOptions, viewModel.isSingleAnswer)
        }
    }

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
                actual_question_number.text = actualQuestion?.code
                actual_question_header_label.setViewVisibility(actualQuestion?.header)
                actual_question_label.text = actualQuestion?.question
                actual_prev_btn.setEnable(state.isPrevEnable)
                actual_next_btn.setEnable(state.isNextEnable)
                actualQuestion?.options?.let {
                    if (it.contains(AREA)) {
                        viewModel.loadAreas()
                    } else {
                        viewModel.currentOptions = it
                    }
                }
                actualQuestion?.type?.let {
                    viewModel.isSingleAnswer = it == SINGLE_ANSWER
                    load_options_label.text = String.format(getString(R.string.load_options_label), it)
                }
            }
        }
    }

    private fun addOptions(selectedList: MutableList<String?>) {
        actual_selection_layout.apply {
            removeAllViews()
            invalidate()
            for (i in selectedList.indices) {
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
                )
                val tv = AppCompatTextView(context)
                TextViewCompat.setTextAppearance(tv, R.style.AvenirHeavy_Black);
                tv.layoutParams = params
                tv.text = "[${i + 1}] ${selectedList[i]}"
                addView(tv)
            }
            actual_next_btn.setEnable(true)
        }
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

    private fun dialogOptions(list: List<String?>, isSingleChoice: Boolean) {
        val listItems = list.toTypedArray()
        val checkedItems = BooleanArray(listItems.size)
        Arrays.fill(checkedItems, false)

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.choose_items)
        val selectedList = mutableListOf<String?>()
        if (isSingleChoice) {
            // add a list
            builder.setItems(listItems) { dialog, which ->
                selectedList.add(listItems[which])
                addOptions(selectedList)
                dialog.dismiss()
            }
        } else {
            // this will checked the items when user open the dialog
            builder.setMultiChoiceItems(listItems, checkedItems) { _, which, isChecked ->
                // Update the current focused item's checked status
                checkedItems[which] = isChecked
            }

            builder.setPositiveButton(DONE) { dialog, _ ->
                for (i in listItems.indices) {
                    val checked = checkedItems[i]
                    if (checked) {
                        selectedList.add(listItems[i])
                    }
                }
                addOptions(selectedList)
                dialog.dismiss()
            }
        }

        val dialog = builder.create()
        dialog.show()
    }

    interface ActualQuestionsFragmentListener {
        fun navigateToActualQuestionsPage2(view: View?)
    }

    companion object {
        private const val SINGLE_ANSWER = "Single Answer"
        private const val DONE = "Done"
        private const val AREA = "Area"
        fun newInstance() = ActualQuestionsFragment()
    }
}