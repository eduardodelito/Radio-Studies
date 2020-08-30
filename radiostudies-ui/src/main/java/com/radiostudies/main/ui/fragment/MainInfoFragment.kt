package com.radiostudies.main.ui.fragment

import com.radiostudies.main.common.fragment.BaseFragment
import com.radiostudies.main.ui.fragment.databinding.MainInfoFragmentBinding
import com.radiostudies.main.ui.viewmodel.MainInfoViewModel
import javax.inject.Inject

class MainInfoFragment : BaseFragment<MainInfoFragmentBinding, MainInfoViewModel>() {

    @Inject
    override lateinit var viewModel: MainInfoViewModel

    override fun createLayout() = R.layout.main_info_fragment

    override fun getBindingVariable() = BR.mainInfoViewModel

    override fun initViews() {
    }

    override fun subscribeUi() {
    }

    companion object {
        fun newInstance() = MainInfoFragment()
    }
}
