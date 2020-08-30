package com.radiostudies.main.ui.fragment

import android.content.Context
import android.view.View
import com.radiostudies.main.common.fragment.BaseFragment
import com.radiostudies.main.ui.fragment.databinding.MainInfoFragmentBinding
import com.radiostudies.main.ui.viewmodel.MainInfoViewModel
import kotlinx.android.synthetic.main.main_info_fragment.*
import javax.inject.Inject

class MainInfoFragment : BaseFragment<MainInfoFragmentBinding, MainInfoViewModel>() {

    private var listener: MainInfoFragmentListener? = null

    @Inject
    override lateinit var viewModel: MainInfoViewModel

    override fun createLayout() = R.layout.main_info_fragment

    override fun getBindingVariable() = BR.mainInfoViewModel

    override fun initViews() {
        continue_button.setOnClickListener {
            listener?.navigateToActualQuestions(it)
        }
    }

    override fun subscribeUi() {
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
        fun navigateToActualQuestions(view: View?)
    }
    companion object {
        fun newInstance() = MainInfoFragment()
    }
}
