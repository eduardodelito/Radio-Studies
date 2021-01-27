package com.radiostudies.main.ui.fragment

import android.content.Context
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.TextViewCompat
import com.radiostudies.main.common.fragment.BaseFragment
import com.radiostudies.main.common.util.*
import com.radiostudies.main.db.entity.Option
import com.radiostudies.main.db.model.DataQuestion
import com.radiostudies.main.ui.fragment.databinding.ActualQuestionsFragmentBinding
import com.radiostudies.main.ui.model.actual.*
import com.radiostudies.main.ui.model.main.MainInfo
import com.radiostudies.main.ui.viewmodel.ActualQuestionsViewModel
import kotlinx.android.synthetic.main.actual_questions_fragment.*
import java.util.*
import javax.inject.Inject


class ActualQuestionsFragment :
    BaseFragment<ActualQuestionsFragmentBinding, ActualQuestionsViewModel>() {

    private var listener: ActualQuestionsFragmentListener? = null
    private var isHours = false
    private lateinit var mainInfo: MainInfo

    @Inject
    override lateinit var viewModel: ActualQuestionsViewModel

    override fun createLayout() = R.layout.actual_questions_fragment

    override fun getBindingVariable() = BR.actualQuestionsViewModel

    override fun initViews() {
        listener?.showAppBar(false)
        mainInfo = arguments?.getSerializable(MAIN_INFO) as MainInfo
        viewModel.initViews(mainInfo.gender)

        activity?.actionBar?.title = getString(R.string.actual_question_title)
        actual_prev_btn.setOnClickListener {
            viewModel.queryActualQuestion(viewModel.minus(), false)
            actual_selection_layout.removeAllViews()
        }
        actual_next_btn.setOnClickListener {
            viewModel.queryActualQuestion(viewModel.plus(), true)
            actual_selection_layout.removeAllViews()
            hideKeyboard()
        }

        load_options_label.setOnClickListener {
            dialogOptions(viewModel.currentOptions, viewModel.isSingleAnswer)
        }

        manual_input.afterTextChanged {
            if (isHours) {
                if (it.isNotEmpty() && it.toInt() <= MAX_HOURS) {
                    actual_next_btn.setEnable(true)
                } else {
                    actual_next_btn.setEnable(false)
                    if (it.isNotEmpty()) {
                        Toast.makeText(
                            context,
                            "Reach the maximum limit of 24 hours.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } else {
                if (it.isNotEmpty()) {
                    actual_next_btn.setEnable(true)
                } else {
                    actual_next_btn.setEnable(false)
                }
            }

        }

        btn_save.setOnClickListener {
            dialogComplete(mainInfo)
        }

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                listener?.exit(String.format(getString(R.string.exit), EXIT))
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun getQuestionLabelText() =
        if (actual_question_header_label.isShown) actual_question_header_label.text.toString() else ""

    override fun subscribeUi() {
        with(viewModel) {
            reObserve(getActualLiveData(), ::onActualQuestionStateChanged)
        }
    }

    private fun onActualQuestionStateChanged(state: ActualQuestionViewState?) {
        when (state) {
            is AreaForm -> {
                viewModel.parseArea(state.fileName?.let {
                    getJsonDataFromAsset(
                        requireContext(),
                        it
                    )
                }, state.genderCode)
            }

            is ActualQuestionForm -> {
                viewModel.parseActualQuestions(state.fileName?.let {
                    getJsonDataFromAsset(
                        requireContext(),
                        it
                    )
                }, state.genderCode)
            }

            is ActualQuestionModel -> {
                val actualQuestion = state.actualQuestion
                actual_question_number.text = actualQuestion.code
                actual_question_header_label.setViewVisibility(actualQuestion.header)
                actual_question_label.text = actualQuestion.question
                actual_prev_btn.setEnable(state.isPrevEnable)
                actual_next_btn.setEnable(state.isNextEnable)
                btn_save.visibility = View.GONE

                when (actualQuestion.options[0].option) {
                    AREA -> {
                        viewModel.loadAreas()
                    }
                    STATIONS_AM -> {
                        viewModel.loadStations(getJsonDataFromAsset(requireContext(), STATION_AM))
                    }
                    STATIONS_FM -> {
                        viewModel.loadStations(getJsonDataFromAsset(requireContext(), STATION_FM))
                    }
                    HOURS -> {
                        isHours = true
                        manual_input.hint = actualQuestion.options[0].option
                        manual_input.inputType = EditorInfo.TYPE_CLASS_NUMBER
                        val maxLength = 2
                        val fArray = arrayOfNulls<InputFilter>(1)
                        fArray[0] = LengthFilter(maxLength)
                        manual_input.filters = fArray
                    }
                    OTHERS -> {
                        isHours = false
                        val hint = actualQuestion.options[0].option
                        actual_next_btn.setEnable(true)
                        manual_input.hint = hint
                        manual_input.inputType = EditorInfo.TYPE_CLASS_TEXT
                        val maxLength = 60
                        val fArray = arrayOfNulls<InputFilter>(1)
                        fArray[0] = LengthFilter(maxLength)
                        manual_input.filters = fArray
                    }
                    EMPLOYER -> {
                        isHours = false
                        val hint = actualQuestion.options[0].option
                        manual_input.hint = hint
                        manual_input.inputType = EditorInfo.TYPE_CLASS_TEXT
                        val maxLength = 60
                        val fArray = arrayOfNulls<InputFilter>(1)
                        fArray[0] = LengthFilter(maxLength)
                        manual_input.filters = fArray
                    }
                    else -> {
                        viewModel.currentOptions = actualQuestion.options
                    }
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
                    manual_input.text.clear()

                } else {
                    line3.visibility = View.VISIBLE
                    load_options_label.visibility = View.VISIBLE
                    manual_input.visibility = View.GONE
                    manual_input.text.clear()
                }

                actualQuestion.code?.let {
                    viewModel.validateCode(it, mainInfo.panelNumber)
                }

                if (!state.isPlus) {
                    viewModel.querySelectedAnswer(actualQuestion.code)
                }
            }

            is SelectedOptionForm -> {
                addOptions(state.selectedOptions)
            }

            is LoadOptions -> {
                if (!state.selectedOptions.isNullOrEmpty()) {
                    val options = Collections.unmodifiableList(state.selectedOptions)
                    addOptions(options)
                }
            }

            is ActualQuestionComplete -> {
                if(state.isComplete) {
                    dialogOption()
                }
            }
        }
    }

    private fun addOptions(selectedList: MutableList<Option>) {
        val selectedOptions = mutableListOf<Option>()
        actual_selection_layout.apply {
            removeAllViews()
            invalidate()
            for (i in selectedList.indices) {
                val option = selectedList[i]
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
                )
                val tv = AppCompatTextView(context)
                TextViewCompat.setTextAppearance(tv, R.style.AvenirHeavy_Black)
                tv.layoutParams = params
                tv.text = String.format(resources.getString(R.string.text_option), i + 1, selectedList[i].option)
                addView(tv)

                if (option.option.contains(OTHER)) {
                    when (option.code) {
                        CODE_10 -> {
                            generateFieldView(this, params, selectedOptions, option.code)
                        }
                        CODE_05_06_07 -> {
                            val maxLength = 60
                            val fArray = arrayOfNulls<InputFilter>(1)

                            val editText1 = EditText(context)
                            editText1.isSingleLine = false
                            editText1.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
                            editText1.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                            fArray[0] = LengthFilter(maxLength)
                            editText1.filters = fArray
                            editText1.isVerticalScrollBarEnabled = true
                            editText1.movementMethod = ScrollingMovementMethod.getInstance()
                            editText1.scrollBarStyle = View.SCROLLBARS_INSIDE_INSET
                            editText1.layoutParams = params

                            val editText2 = EditText(context)
                            editText2.isSingleLine = false
                            editText2.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
                            editText2.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                            fArray[0] = LengthFilter(maxLength)
                            editText2.filters = fArray
                            editText2.isVerticalScrollBarEnabled = true
                            editText2.movementMethod = ScrollingMovementMethod.getInstance()
                            editText2.scrollBarStyle = View.SCROLLBARS_INSIDE_INSET
                            editText2.layoutParams = params

                            val editText3 = EditText(context)
                            editText3.isSingleLine = false
                            editText3.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
                            editText3.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                            fArray[0] = LengthFilter(maxLength)
                            editText3.filters = fArray
                            editText3.isVerticalScrollBarEnabled = true
                            editText3.movementMethod = ScrollingMovementMethod.getInstance()
                            editText3.scrollBarStyle = View.SCROLLBARS_INSIDE_INSET
                            editText3.layoutParams = params

                            editText1.afterTextChanged {
                                selectedOptions.clear()
                                selectedOptions.add(Option(CODE_05, editText1.text.toString()))
                                selectedOptions.add(Option(CODE_06, editText2.text.toString()))
                                selectedOptions.add(Option(CODE_05, editText3.text.toString()))
                                viewModel.saveQuestion(
                                    DataQuestion(
                                        actual_question_number.text.toString(),
                                        getQuestionLabelText(),
                                        actual_question_label.text.toString(),
                                        selectedOptions
                                    )
                                )
                            }

                            editText2.afterTextChanged {
                                selectedOptions.clear()
                                selectedOptions.add(Option(CODE_05, editText1.text.toString()))
                                selectedOptions.add(Option(CODE_06, editText2.text.toString()))
                                selectedOptions.add(Option(CODE_07, editText3.text.toString()))
                                viewModel.saveQuestion(
                                    DataQuestion(
                                        actual_question_number.text.toString(),
                                        getQuestionLabelText(),
                                        actual_question_label.text.toString(),
                                        selectedOptions
                                    )
                                )
                            }

                            editText3.afterTextChanged {
                                selectedOptions.clear()
                                selectedOptions.add(Option(CODE_05, editText1.text.toString()))
                                selectedOptions.add(Option(CODE_06, editText2.text.toString()))
                                selectedOptions.add(Option(CODE_07, editText3.text.toString()))
                                viewModel.saveQuestion(
                                    DataQuestion(
                                        actual_question_number.text.toString(),
                                        getQuestionLabelText(),
                                        actual_question_label.text.toString(),
                                        selectedOptions
                                    )
                                )
                            }

                            selectedOptions.add(Option(CODE_05, editText1.text.toString()))
                            selectedOptions.add(Option(CODE_06, editText2.text.toString()))
                            selectedOptions.add(Option(CODE_05, editText3.text.toString()))
                            viewModel.saveQuestion(
                                DataQuestion(
                                    actual_question_number.text.toString(),
                                    getQuestionLabelText(),
                                    actual_question_label.text.toString(),
                                    selectedOptions
                                )
                            )

                            addView(editText1)
                            addView(editText2)
                            addView(editText3)
                        }
                        else -> {
                            generateFieldView(this, params, selectedOptions, "0")
                        }
                    }
                } else {
                    selectedOptions.add(Option(option.code, option.option))
                    viewModel.saveQuestion(
                        DataQuestion(
                            actual_question_number.text.toString(),
                            getQuestionLabelText(),
                            actual_question_label.text.toString(),
                            selectedOptions
                        )
                    )
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

    private fun generateFieldView(
        view: LinearLayout,
        params: LinearLayout.LayoutParams,
        selectedOptions: MutableList<Option>,
        code: String
    ) {
        val editText = EditText(context)
        editText.isSingleLine = false
        editText.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
        editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        val maxLength = 60
        val fArray = arrayOfNulls<InputFilter>(1)
        fArray[0] = LengthFilter(maxLength)
        editText.filters = fArray
        editText.isVerticalScrollBarEnabled = true
        editText.movementMethod = ScrollingMovementMethod.getInstance()
        editText.scrollBarStyle = View.SCROLLBARS_INSIDE_INSET
        editText.layoutParams = params

        editText.afterTextChanged {
            selectedOptions.clear()
            selectedOptions.add(Option(code, it))
            viewModel.saveQuestion(
                DataQuestion(
                    actual_question_number.text.toString(),
                    getQuestionLabelText(),
                    actual_question_label.text.toString(),
                    selectedOptions
                )
            )
        }
        selectedOptions.add(Option(code, editText.text.toString()))
        viewModel.saveQuestion(
            DataQuestion(
                actual_question_number.text.toString(),
                getQuestionLabelText(),
                actual_question_label.text.toString(),
                selectedOptions
            )
        )
        view.addView(editText)
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
                if (actual_question_label.text.toString() == AREA) {
                    viewModel.selectedArea = item
                }
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

                    listItems[which].contains(OTHER_APPS) -> {
                        for (i in listItems.indices) {
                            if (!listItems[i].contains(OTHER_APPS)) {
                                dListView.setItemChecked(i, false)
                                checkedItems[i] = false
                            }
                        }
                    }

                    listItems[which].contains(NOT_LISTEN) -> {
                        for (i in listItems.indices) {
                            if (!listItems[i].contains(NOT_LISTEN)) {
                                dListView.setItemChecked(i, false)
                                checkedItems[i] = false
                            }
                        }
                    }
                    else -> {
                        for (i in listItems.indices) {
                            when {
                                listItems[i].contains(NONE) -> {
                                    dListView.setItemChecked(i, false)
                                    checkedItems[i] = false
                                }
                                listItems[i].contains(OTHER) -> {
                                    dListView.setItemChecked(i, false)
                                    checkedItems[i] = false
                                }
                                listItems[i].contains(OTHER_APPS) -> {
                                    dListView.setItemChecked(i, false)
                                    checkedItems[i] = false
                                }
                                listItems[i].contains(NOT_LISTEN) -> {
                                    dListView.setItemChecked(i, false)
                                    checkedItems[i] = false
                                }
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


    private fun dialogComplete(mainInfo: MainInfo?) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(R.string.complete_msg)
        builder.setPositiveButton(getString(R.string.yes_label)) { dialog, _ ->
            viewModel.saveActualQuestions(mainInfo)
            dialog.dismiss()
        }
        builder.setNegativeButton(getString(R.string.no_label)) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun dialogOption() {
        // setup the alert builder
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setTitle(R.string.success)
        // add a list
        val options = arrayOf("Other Panel number", "Other member number", "Exit")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> {
                    viewModel.setClearPanelAndMemberNumber(true)
                    listener?.navigateBack()
                }
                1 -> {
                    viewModel.setClearPanelAndMemberNumber(false)
                    listener?.navigateBack()
                }
                2 -> {
                    listener?.completeAndClose()
                }
            }
            dialog.dismiss()
        }

        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
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
        fun exit(message: String?)

        fun showAppBar(show: Boolean)

        fun completeAndClose()

        fun navigateBack()
    }

    companion object {
        private const val SINGLE_ANSWER = "Single Answer"
        private const val DONE = "Done"
        private const val AREA = "Area"
        private const val STATIONS_AM = "stations_am"
        private const val STATIONS_FM = "stations_fm"
        private const val STATION_AM = "radio_stations_am.json"
        private const val STATION_FM = "radio_stations_fm.json"
        private const val HOURS = "hour/s"
        private const val OTHER = "Other"
        private const val OTHER_APPS = "Other Apps"
        private const val EMPLOYER = "Employer"
        private const val OTHERS = "Others (Specify)"
        private const val NONE = "None"
        private const val NOT_LISTEN = "Not Listen"
        private const val MAIN_INFO = "main_info"
        private const val MAX_HOURS = 24
        private const val EXIT = "exit Actual Questions"

        private const val CODE_05 = "05"
        private const val CODE_06 = "06"
        private const val CODE_07 = "07"
        private const val CODE_10 = "10"
        private const val CODE_05_06_07 = "05,06,07"

        fun newInstance() = ActualQuestionsFragment()
    }
}