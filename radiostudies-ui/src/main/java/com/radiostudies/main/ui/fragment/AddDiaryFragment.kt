package com.radiostudies.main.ui.fragment

import android.content.Context
import android.text.InputFilter
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
import com.radiostudies.main.common.util.getJsonDataFromAsset
import com.radiostudies.main.common.util.reObserve
import com.radiostudies.main.common.util.setEnable
import com.radiostudies.main.db.entity.Option
import com.radiostudies.main.ui.fragment.databinding.AddDiaryFragmentBinding
import com.radiostudies.main.ui.model.diary.AddDiaryForm
import com.radiostudies.main.ui.model.diary.DiaryModelState
import com.radiostudies.main.ui.viewmodel.AddDiaryViewModel
import kotlinx.android.synthetic.main.add_diary_fragment.*
import java.util.*
import javax.inject.Inject

class AddDiaryFragment : BaseFragment<AddDiaryFragmentBinding, AddDiaryViewModel>() {

    private var listener: AddDiaryFragmentListener? = null

    @Inject
    override lateinit var viewModel: AddDiaryViewModel

    override fun createLayout() = R.layout.add_diary_fragment

    override fun getBindingVariable() = BR.addDiaryViewModel

    override fun initViews() {
        listener?.showAppBar(false)

        add_time_of_listening.setOnClickListener {
            dialogOptions(add_time_of_listening_layout, viewModel.timeOfListeningList, false)
        }

        add_radio_stations.setOnClickListener {

        }

        add_place_of_listening.setOnClickListener {

        }

        add_details_device.setOnClickListener {

        }

        btn_add_diary.setOnClickListener {
            listener?.navigateBack()
        }
        btn_add_diary.setEnable(false)
    }

    override fun subscribeUi() {
        with(viewModel) {
            reObserve(getDiaryLiveData(), ::onDiaryStateChanged)
        }
    }

    private fun onDiaryStateChanged(state: DiaryModelState?) {
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

    private fun dialogOptions(view: LinearLayout, list: List<Option>, isSingleChoice: Boolean) {
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
        var selectedOptions = mutableListOf<Option>()
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

            //Save here
        }
    }

    companion object {
        private const val OTHER = "Other"
        private const val NONE = "None"
        private const val DONE = "Done"

        fun newInstance() = AddDiaryFragment()
    }
}
