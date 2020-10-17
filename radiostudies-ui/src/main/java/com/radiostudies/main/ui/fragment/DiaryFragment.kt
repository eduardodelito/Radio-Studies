package com.radiostudies.main.ui.fragment

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.radiostudies.main.common.fragment.BaseFragment
import com.radiostudies.main.common.util.reObserve
import com.radiostudies.main.ui.adapter.DiaryAdapter
import com.radiostudies.main.ui.fragment.databinding.DiaryFragmentBinding
import com.radiostudies.main.ui.model.diary.DiaryForm
import com.radiostudies.main.ui.model.diary.DiaryModel
import com.radiostudies.main.ui.model.diary.DiaryModelState
import com.radiostudies.main.ui.viewmodel.DiaryViewModel
import kotlinx.android.synthetic.main.diary_fragment.*
import javax.inject.Inject

class DiaryFragment : BaseFragment<DiaryFragmentBinding, DiaryViewModel>() {

    private var listener: OnDiaryFragmentListener? = null

    private lateinit var diaryAdapter: DiaryAdapter

    @Inject
    override lateinit var viewModel: DiaryViewModel

    override fun createLayout() = R.layout.diary_fragment

    override fun getBindingVariable() = BR.diaryViewModel

    override fun initViews() {
        diaryAdapter = DiaryAdapter(object: DiaryAdapter.OnDiaryAdapterListener {
            override fun onItemSelected(view: View, diaryModel: DiaryModel?) {
                listener?.navigateToDiaryDetails(view, diaryModel)
            }
        })
        with(recycler_view) {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    (layoutManager as LinearLayoutManager).orientation
                )
            )
            adapter = diaryAdapter
        }

    }

    override fun subscribeUi() {
        with(viewModel) {
            reObserve(getDiaryLiveData(), ::onDiaryStateChanged)
            loadDiary()
        }
    }

    private fun onDiaryStateChanged(state: DiaryModelState?) {
        when(state) {
            is DiaryForm -> {
                diaryAdapter.updateData(state.diaryList)
                if (state.diaryList.isEmpty()) {
                    dialog()
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDiaryFragmentListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnDiaryFragmentListener {
        fun navigateToDiaryDetails(view: View, diaryModel: DiaryModel?)

        fun navigateBack()
    }

    private fun dialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.no_diary_available_msg)
        builder.setPositiveButton(getString(R.string.ok_label)) { dialog, _ ->
            listener?.navigateBack()
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    companion object {
        const val DIARY_ITEM = "diary_item"

        fun newInstance() = DiaryFragment()
    }
}