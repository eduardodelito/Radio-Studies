package com.radiostudies.main.ui.fragment

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.radiostudies.main.common.fragment.BaseFragment
import com.radiostudies.main.common.util.reObserve
import com.radiostudies.main.ui.adapter.DiaryAdapter
import com.radiostudies.main.ui.fragment.databinding.DiaryFragmentBinding
import com.radiostudies.main.ui.model.diary.DiaryForm
import com.radiostudies.main.ui.model.diary.DiaryModelState
import com.radiostudies.main.ui.viewmodel.DiaryViewModel
import kotlinx.android.synthetic.main.diary_fragment.*
import javax.inject.Inject

class DiaryFragment : BaseFragment<DiaryFragmentBinding, DiaryViewModel>() {

    private lateinit var diaryAdapter: DiaryAdapter

    @Inject
    override lateinit var viewModel: DiaryViewModel

    override fun createLayout() = R.layout.diary_fragment

    override fun getBindingVariable() = BR.diaryViewModel

    override fun initViews() {
        diaryAdapter = DiaryAdapter()
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
        }
    }

    private fun onDiaryStateChanged(state: DiaryModelState?) {
        when(state) {
            is DiaryForm -> {
                diaryAdapter.updateData(state.diaryList)
            }
        }
    }

    companion object {
        fun newInstance() = DiaryFragment()
    }
}