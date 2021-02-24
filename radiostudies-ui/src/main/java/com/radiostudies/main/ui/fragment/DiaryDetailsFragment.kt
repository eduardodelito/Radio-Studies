package com.radiostudies.main.ui.fragment

import android.content.Context
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.TextViewCompat
import com.radiostudies.main.common.fragment.BaseFragment
import com.radiostudies.main.common.util.reObserve
import com.radiostudies.main.model.Diaries
import com.radiostudies.main.model.Diary
import com.radiostudies.main.ui.fragment.databinding.DiaryDetailsFragmentBinding
import com.radiostudies.main.ui.model.DiaryDetailsForm
import com.radiostudies.main.ui.model.DiaryDetailsViewState
import com.radiostudies.main.model.DiaryModel
import com.radiostudies.main.ui.viewmodel.DiaryDetailsViewModel
import kotlinx.android.synthetic.main.diary_details_fragment.*
import javax.inject.Inject


class DiaryDetailsFragment : BaseFragment<DiaryDetailsFragmentBinding, DiaryDetailsViewModel>() {

    private var listener: DiaryDetailsFragmentListener? = null
    private var diaryModel: DiaryModel? = null

    @Inject
    override lateinit var viewModel: DiaryDetailsViewModel

    override fun createLayout() = R.layout.diary_details_fragment

    override fun getBindingVariable() = BR.diaryDetailsViewModel

    override fun initViews() {
        listener?.showAppBar(true)
        setHasOptionsMenu(true)
        diaryModel = arguments?.getSerializable(DiaryFragment.DIARY_ITEM) as DiaryModel?
        updateDetails(diaryModel)
    }

    override fun subscribeUi() {
        with(viewModel) {
            reObserve(getDiaryDetailsLiveData(), ::onDiaryDetailsStateChanged)
            diaryModel?.diary?.mainInfo?.let { loadDiaries(it) }
        }
    }

    private fun onDiaryDetailsStateChanged(state: DiaryDetailsViewState?) {
        when (state) {
            is DiaryDetailsForm -> {
                addDetailsItem(state.diaries)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_diary, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_menu -> {
                diaryModel?.diary?.let {
                    listener?.navigateToAddDiaryScreen(
                        requireView(), diaryModel?.selectedArea,
                        it
                    )
                }
                true
            }
            R.id.send_menu -> {
                viewModel.sendDiaries(diaryModel)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addDetailsItem(diaries: List<Diaries>) {

        details_items_layout.apply {
            removeAllViews()
            invalidate()
            for (i in diaries.indices) {

                val view = LayoutInflater.from(context).inflate(R.layout.details_item_diary, null)
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.topMargin = 20
                view.layoutParams = params
                val delete = view.findViewById<ImageView>(R.id.btn_delete)
                delete.setOnClickListener {
                    diaryModel?.diary?.let { diary -> deleteDialog(diary, diaries, diaries[i]) }
                }

                val layout0 = view.findViewById<LinearLayout>(R.id.details_day_of_study_layout)
                val dayOfStudyList = diaries[i].dayOfStudy
                for (g in dayOfStudyList.indices) {
                    layout0.visibility = View.VISIBLE
                    val tv0 = AppCompatTextView(context)
                    TextViewCompat.setTextAppearance(tv0, R.style.AvenirHeavy_Black)
                    tv0.layoutParams = params
                    tv0.text = dayOfStudyList[g].option
                    layout0.addView(tv0)
                }

                val layout1 = view.findViewById<LinearLayout>(R.id.details_diary_date_layout)
                val diaryDateList = diaries[i].diaryDate
                for (h in diaryDateList.indices) {
                    layout1.visibility = View.VISIBLE
                    val tv1 = AppCompatTextView(context)
                    TextViewCompat.setTextAppearance(tv1, R.style.AvenirHeavy_Black)
                    tv1.layoutParams = params
                    tv1.text = diaryDateList[h].option
                    layout1.addView(tv1)
                }

                val layout2 = view.findViewById<LinearLayout>(R.id.details_time_of_listening_layout)
                val timeOfListeningList = diaries[i].timeOfListening
                for (j in timeOfListeningList.indices) {
                    layout2.visibility = View.VISIBLE
                    val tv2 = AppCompatTextView(context)
                    TextViewCompat.setTextAppearance(tv2, R.style.AvenirHeavy_Black)
                    tv2.layoutParams = params
                    tv2.text = timeOfListeningList[j].option
                    layout2.addView(tv2)
                }

                val layout3 = view.findViewById<LinearLayout>(R.id.details_radio_stations_layout)
                val radioStationList = diaries[i].stations
                for (k in radioStationList.indices) {
                    layout3.visibility = View.VISIBLE
                    val tv3 = AppCompatTextView(context)
                    TextViewCompat.setTextAppearance(tv3, R.style.AvenirHeavy_Black)
                    tv3.layoutParams = params
                    tv3.text = radioStationList[k].option
                    layout3.addView(tv3)
                }

                val layout4 =
                    view.findViewById<LinearLayout>(R.id.details_place_of_listening_layout)
                val placeOfListeningList = diaries[i].placeOfListening
                for (l in placeOfListeningList.indices) {
                    layout4.visibility = View.VISIBLE
                    val tv4 = AppCompatTextView(context)
                    TextViewCompat.setTextAppearance(tv4, R.style.AvenirHeavy_Black)
                    tv4.layoutParams = params
                    tv4.text = radioStationList[l].option
                    layout4.addView(tv4)
                }

                val layout5 = view.findViewById<LinearLayout>(R.id.details_device_layout)
                val deviceList = diaries[i].device
                for (m in deviceList.indices) {
                    layout5.visibility = View.VISIBLE
                    val tv5 = AppCompatTextView(context)
                    TextViewCompat.setTextAppearance(tv5, R.style.AvenirHeavy_Black)
                    tv5.layoutParams = params
                    tv5.text = deviceList[m].option
                    layout5.addView(tv5)
                }

                addView(view)
            }
        }
    }

    private fun updateDetails(mDiaryModel: DiaryModel?) {
        with(viewModel) {
            diaryModel = mDiaryModel
        }
        getBinding().executePendingBindings()
        getBinding().invalidateAll()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DiaryDetailsFragmentListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface DiaryDetailsFragmentListener {
        fun showAppBar(show: Boolean)

        fun navigateToAddDiaryScreen(view: View, selectedArea: String?, diary: Diary)
    }

    private fun deleteDialog(diary: Diary, diaryList: List<Diaries>, diaries: Diaries) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.delete_msg)
        builder.setPositiveButton(getString(R.string.yes_label)) { dialog, _ ->
            viewModel.deleteSelectedDiaries(diary, diaryList, diaries)
            dialog.dismiss()
        }
        builder.setNegativeButton(getString(R.string.no_label)) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    companion object {
        fun newInstance() = DiaryDetailsFragment()
    }
}
