package com.radiostudies.main.ui.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.TextViewCompat
import com.radiostudies.main.common.fragment.BaseFragment
import com.radiostudies.main.common.util.getJsonDataFromAsset
import com.radiostudies.main.common.util.reObserve
import com.radiostudies.main.common.util.setEnable
import com.radiostudies.main.db.entity.Option
import com.radiostudies.main.db.model.Diary
import com.radiostudies.main.ui.fragment.databinding.AddDiaryFragmentBinding
import com.radiostudies.main.ui.model.diary.*
import com.radiostudies.main.ui.viewmodel.AddDiaryViewModel
import kotlinx.android.synthetic.main.add_diary_fragment.*
import java.util.*
import javax.inject.Inject

class AddDiaryFragment : BaseFragment<AddDiaryFragmentBinding, AddDiaryViewModel>(),
    DatePickerDialog.OnDateSetListener {

    private var listener: AddDiaryFragmentListener? = null
    private val cal: Calendar = Calendar.getInstance()

    @Inject
    override lateinit var viewModel: AddDiaryViewModel

    override fun createLayout() = R.layout.add_diary_fragment

    override fun getBindingVariable() = BR.addDiaryViewModel

    override fun initViews() {
        listener?.showAppBar(false)
        val selectedArea = arguments?.getString(AREA)
        val selectedDiary = arguments?.getSerializable(DIARY) as Diary

        add_day_of_study.setOnClickListener {
            viewModel.dayOfStudy()
        }

        add_diary_date.setOnClickListener {
            openDatePicker()
        }

        add_time_of_listening.setOnClickListener {
            val mainInfo = selectedDiary.mainInfo as String
            viewModel.queryTimeOfListening(mainInfo)
        }

        add_radio_stations.setOnClickListener {
            viewModel.loadStations(getJsonDataFromAsset(requireContext(), STATIONS), selectedArea)
        }

        add_place_of_listening.setOnClickListener {
            viewModel.placeOfListening()
        }

        add_details_device.setOnClickListener {
            viewModel.device()
        }

        btn_add_diary.setOnClickListener {
            viewModel.updateAndSaveDiary(selectedDiary)
        }
        btn_add_diary.setEnable(false)
    }

    override fun subscribeUi() {
        with(viewModel) {
            reObserve(getDiaryLiveData(), ::onDiaryStateChanged)
        }
    }

    private fun onDiaryStateChanged(state: AddDiaryModelState?) {
        when (state) {
            is AddDiaryForm -> {
                viewModel.parseTimeOfListening(
                    state.fileName?.let {
                        getJsonDataFromAsset(
                            requireContext(),
                            it
                        )
                    }
                )
            }

            is StationsForm -> {
                dialogOptions(add_radio_stations_layout, state.list, true, viewModel.selectedStations)
            }

            is AddDayOfStudyForm -> {
                dialogOptions(add_day_of_study_layout, state.list, true, viewModel.selectedDayOfStudy)
            }

            is TimeOfListeningForm -> {
                dialogOptions(add_time_of_listening_layout, state.list, false, viewModel.selectedTimeOfListeningList)
            }

            is PlaceOfListeningForm -> {
                dialogOptions(add_place_of_listening_layout, state.list, true, viewModel.selectedPlaceOfListening)
            }

            is DeviceForm -> {
                dialogOptions(add_device_layout, state.list, true, viewModel.selectedDevice)
            }

            is CompletedDiaryForm -> {
                if (state.isCompleted) {
                    listener?.navigateBack()
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AddDiaryFragmentListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface AddDiaryFragmentListener {
        fun showAppBar(show: Boolean)

        fun navigateBack()
    }

    private fun dialogOptions(view: LinearLayout, list: List<Option>, isSingleChoice: Boolean, selectedList: MutableList<Option>) {
        val listStr = mutableListOf<String>()
        list.forEach { listStr.add(it.option) }

        val listItems = listStr.toTypedArray()
        val checkedItems = BooleanArray(listItems.size)
        Arrays.fill(checkedItems, false)

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.choose_items)
        selectedList.clear()
        if (isSingleChoice) {
            // add a list
            builder.setItems(listItems) { dialog, which ->
                val item = listItems[which]
                val code = list.find { option -> option.option == item }?.code.toString()

                selectedList.add(Option(code, item))
                addOptions(view, selectedList)
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
                        val code = list.find { option -> option.option == item }?.code.toString()
                        selectedList.add(Option(code, item))
                    }
                }
                addOptions(view, selectedList)
                dialog.dismiss()
            }
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun addOptions(view: LinearLayout, selectedList: MutableList<Option>) {
        val selectedOptions = mutableListOf<Option>()
        view.apply {
            visibility = if (selectedList.isEmpty()) View.GONE else View.VISIBLE
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
                    val editText = EditText(context)
                    editText.isSingleLine = false
                    editText.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
                    editText.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                    val maxLength = 60
                    val fArray = arrayOfNulls<InputFilter>(1)
                    fArray[0] = InputFilter.LengthFilter(maxLength)
                    editText.filters = fArray
                    editText.isVerticalScrollBarEnabled = true
                    editText.movementMethod = ScrollingMovementMethod.getInstance()
                    editText.scrollBarStyle = View.SCROLLBARS_INSIDE_INSET
                    editText.layoutParams = params
                    addView(editText)
                    selectedOptions.add(Option("0", editText.text.toString()))
                } else {
                    selectedOptions.add(Option(option.code, option.option))
                }
            }
            btn_add_diary.setEnable(isLayoutsHasContent())
        }
    }

    private fun openDatePicker() {
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)
        val datePickerDialog = DatePickerDialog(requireActivity(), this, year, month, day)
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        add_diary_date_layout.apply {
            removeAllViews()
            invalidate()
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val tv = AppCompatTextView(context)
            TextViewCompat.setTextAppearance(tv, R.style.AvenirHeavy_Black)
            tv.layoutParams = params
            val selectedDateValue = String.format(resources.getString(R.string.date_of_interview), month + 1, dayOfMonth, year)
            tv.text = selectedDateValue
            viewModel.selectedDiaryDate.add(Option("1", selectedDateValue))
            addView(tv)
            visibility = if (selectedDateValue.isNullOrEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun isLayoutsHasContent() =
        add_time_of_listening_layout.childCount > 0 &&
                add_radio_stations_layout.childCount > 0 &&
                add_place_of_listening_layout.childCount > 0 &&
                add_device_layout.childCount > 0

    companion object {
        private const val OTHER = "Other"
        private const val NONE = "None"
        private const val DONE = "Done"
        const val AREA = "area"
        const val DIARY = "diary"

        private const val STATIONS = "radio_stations.json"

        fun newInstance() = AddDiaryFragment()
    }
}
