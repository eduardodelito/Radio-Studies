package com.radiostudies.main.ui.fragment

import com.radiostudies.main.common.fragment.BaseFragment
import com.radiostudies.main.ui.fragment.databinding.DiaryFragmentBinding
import com.radiostudies.main.ui.viewmodel.DiaryViewModel
import javax.inject.Inject

class DiaryFragment : BaseFragment<DiaryFragmentBinding, DiaryViewModel>() {

    companion object {
        fun newInstance() = DiaryFragment()
    }

    @Inject
    override lateinit var viewModel: DiaryViewModel

    override fun createLayout() = R.layout.diary_fragment

    override fun getBindingVariable() = BR.diaryViewModel

    override fun subscribeUi() {

    }

    override fun initViews() {

    }
}