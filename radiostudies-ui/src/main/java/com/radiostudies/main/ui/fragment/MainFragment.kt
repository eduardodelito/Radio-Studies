package com.radiostudies.main.ui.fragment

import com.radiostudies.main.common.fragment.BaseFragment
import com.radiostudies.main.ui.fragment.databinding.MainFragmentBinding
import com.radiostudies.main.ui.viewmodel.MainViewModel
import javax.inject.Inject

class MainFragment : BaseFragment<MainFragmentBinding, MainViewModel>() {

    companion object {
        fun newInstance() = MainFragment()
    }

    @Inject
    override lateinit var viewModel: MainViewModel

    override fun createLayout() = R.layout.main_fragment

    override fun getBindingVariable() = BR.mainViewModel

    override fun initViews() {

    }

    override fun subscribeUi() {

    }
}
