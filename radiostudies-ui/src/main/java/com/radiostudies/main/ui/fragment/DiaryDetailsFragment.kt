package com.radiostudies.main.ui.fragment

import com.radiostudies.main.common.fragment.BaseFragment
import com.radiostudies.main.ui.fragment.databinding.DiaryDetailsFragmentBinding
import com.radiostudies.main.ui.model.diary.DiaryModel
import com.radiostudies.main.ui.viewmodel.DiaryDetailsViewModel
import javax.inject.Inject

class DiaryDetailsFragment : BaseFragment<DiaryDetailsFragmentBinding, DiaryDetailsViewModel>() {

    companion object {
        fun newInstance() = DiaryDetailsFragment()
    }

    @Inject
    override lateinit var viewModel: DiaryDetailsViewModel

    override fun createLayout() = R.layout.diary_details_fragment

    override fun getBindingVariable() = BR.diaryDetailsViewModel

    override fun initViews() {
        val diaryModel = arguments?.getSerializable(DiaryFragment.DIARY_ITEM) as DiaryModel?
        println("memberNumber=========${diaryModel?.memberNumber}")
    }

    override fun subscribeUi() {

    }
}