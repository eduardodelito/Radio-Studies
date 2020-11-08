package com.radiostudies.main.ui.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.radiostudies.main.common.fragment.BaseFragment
import com.radiostudies.main.common.util.reObserve
import com.radiostudies.main.ui.fragment.databinding.MainInfoFragmentBinding
import com.radiostudies.main.ui.model.main.MainInfo
import com.radiostudies.main.ui.model.main.MainInfoData
import com.radiostudies.main.ui.model.main.MainInfoForm
import com.radiostudies.main.ui.model.main.MainInfoState
import com.radiostudies.main.ui.viewmodel.MainInfoViewModel
import kotlinx.android.synthetic.main.main_info_fragment.*
import java.util.*
import javax.inject.Inject

class MainInfoFragment : BaseFragment<MainInfoFragmentBinding, MainInfoViewModel>(),
    DatePickerDialog.OnDateSetListener {

    private var listener: MainInfoFragmentListener? = null
    private val cal: Calendar = Calendar.getInstance()

    @Inject
    override lateinit var viewModel: MainInfoViewModel

    override fun createLayout() = R.layout.main_info_fragment

    override fun getBindingVariable() = BR.mainInfoViewModel

    override fun initViews() {
        listener?.showAppBar(false)
        continue_button.setOnClickListener {

            viewModel.mainInfoDataChanged(
                panel_number_field.text.toString(),
                member_number_field.text.toString(),
                municipality_field.text.toString(),
                barangay_field.text.toString(),
                name_of_respondent_field.text.toString(),
                address_field.text.toString(),
                age_field.text.toString(),
                gender_field.tag.toString(),
                date_of_interview_field.text.toString(),
                time_start_field.text.toString(),
                viewModel.getCode().toString(),
                contact_number_field.text.toString(),
                eco_class_label.tag.toString()
            )
        }

        date_of_interview_field.setText(viewModel.getDate())

        time_start_field.setText(viewModel.getTime())

//        time_end_field.setOnClickListener {
//            openTimePicker(time_end_field)
//        }

        day_of_week_field.setText(viewModel.getDay())

        eco_class_label.apply {
            setOnClickListener {
                dialogList(this, arrayOf(AB, C1, C2, D, E))
            }
        }

        gender_field.apply {
            setOnClickListener {
                dialogList(this, arrayOf(MALE, FEMALE))
            }
        }
    }

    override fun subscribeUi() {
        with(viewModel) {
            reObserve(getMainInfoLiveData(), ::onMainInfoStateChanged)
        }
    }

    private fun onMainInfoStateChanged(state: MainInfoState?) {
        when (state) {
            is MainInfoForm -> {
                if (state.panelNumber != null) {
                    panel_number_field.error = getString(state.panelNumber)
                    panel_number_field.requestFocus()
                }

                if (state.memberNumber != null) {
                    member_number_field.error = getString(state.memberNumber)
                    member_number_field.requestFocus()
                }

                if (state.municipality != null) {
                    municipality_field.error = getString(state.municipality)
                    municipality_field.requestFocus()
                }

                if (state.barangay != null) {
                    barangay_field.error = getString(state.barangay)
                    barangay_field.requestFocus()
                }

                if (state.nameOfRespondent != null) {
                    name_of_respondent_field.error = getString(state.nameOfRespondent)
                    name_of_respondent_field.requestFocus()
                }

                if (state.address != null) {
                    address_field.error = getString(state.address)
                    address_field.requestFocus()
                }

                if (state.age != null) {
                    age_field.error = getString(state.age)
                    age_field.requestFocus()
                }

                if (state.dateOfInterview != null) {
                    date_of_interview_field.error = getString(state.dateOfInterview)
                    date_of_interview_field.requestFocus()
                }

                if (state.timeStart != null) {
                    time_start_field.error = getString(state.timeStart)
                    time_start_field.requestFocus()
                }

//                if (state.timeEnd != null) {
//                    time_end_field.error = getString(state.timeEnd)
//                    time_end_field.requestFocus()
//                }

                if (state.dayOfWeek != null) {
                    day_of_week_field.error = getString(state.dayOfWeek)
                    day_of_week_field.requestFocus()
                }

                if (state.contactNumber != null) {
                    contact_number_field.error = getString(state.contactNumber)
                    contact_number_field.requestFocus()
                }

                if (state.ecoClass != null) {
                    eco_class_label.error = getString(state.ecoClass)
                    eco_class_label.requestFocus()
                }
            }

            is MainInfoData -> {
                listener?.navigateToActualQuestions(view, state.mainInfo)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainInfoFragmentListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface MainInfoFragmentListener {
        fun navigateToActualQuestions(view: View?, mainInfo: MainInfo?)

        fun showAppBar(show: Boolean)
    }

    private fun openDatePicker() {
        var day = cal.get(Calendar.DAY_OF_MONTH)
        var month = cal.get(Calendar.MONTH)
        var year = cal.get(Calendar.YEAR)
        val datePickerDialog = DatePickerDialog(requireActivity(), this, year, month, day)
        datePickerDialog.show()
    }

//    private fun openTimePicker(view: EditText) {
//        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
//            cal.set(Calendar.HOUR_OF_DAY, hour)
//            cal.set(Calendar.MINUTE, minute)
//
//            val sdf = SimpleDateFormat(TIME_FORMAT, Locale.US)
//            val formattedTime = sdf.format(cal.time) //format your time
//            view.setText(formattedTime)
//        }
//        TimePickerDialog(
//            requireContext(),
//            timeSetListener,
//            cal.get(Calendar.HOUR_OF_DAY),
//            cal.get(Calendar.MINUTE),
//            false
//        ).show()
//    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        date_of_interview_field.setText("${month + 1}/$dayOfMonth/$year")
    }

    private fun dialogList(field: EditText, list: Array<String>) {
        // setup the alert builder
        val builder = AlertDialog.Builder(requireContext())

        // add a list
        builder.setItems(list) { dialog, which ->
            field.setText(list[which])
            field.tag = which + 1
            dialog.dismiss()
        }

        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    companion object {
        private const val MALE = "MALE"
        private const val FEMALE = "FEMALE"

        private const val AB = "AB"
        private const val C1 = "C1"
        private const val C2 = "C2"
        private const val D = "D"
        private const val E = "E"

        private const val TIME_FORMAT = "hh:mm:ss a"
        const val MAIN_INFO = "main_info"

        fun newInstance() = MainInfoFragment()
    }
}
