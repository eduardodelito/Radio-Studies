package com.radiostudies.main.ui.fragment

import android.content.Context
import android.view.View
import com.radiostudies.main.common.fragment.BaseFragment
import com.radiostudies.main.ui.fragment.databinding.InitialQuestionsFragmentBinding
import com.radiostudies.main.ui.viewmodel.InitialQuestionsViewModel
import kotlinx.android.synthetic.main.initial_questions_fragment.*
import javax.inject.Inject

class InitialQuestionsFragment :
    BaseFragment<InitialQuestionsFragmentBinding, InitialQuestionsViewModel>(),
    View.OnClickListener {

    private var listener: InitialQuestionsFragmentListener? = null

    @Inject
    override lateinit var viewModel: InitialQuestionsViewModel

    override fun createLayout() = R.layout.initial_questions_fragment

    override fun getBindingVariable() = BR.initialViewModel

    override fun initViews() {
        continue_button.setOnClickListener(this)
        agree_radio_btn.setOnClickListener(this)
        disagree_radio_btn.setOnClickListener(this)
    }

    override fun subscribeUi() {
    }

    override fun onClick(view: View) {
        when (view.id) {
            continue_button.id -> {
//                var id: Int = radio_group1.checkedRadioButtonId
//                if (id != -1) {
//                    val radio = radio_group1.findViewById(id) as AppCompatRadioButton
//                    Toast.makeText(
//                        context, "On button click : ${radio.text}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    Toast.makeText(
//                        context, "On button click : nothing selected",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
                listener?.navigateToMainInfo(view)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is InitialQuestionsFragmentListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface InitialQuestionsFragmentListener {
        fun navigateToMainInfo(view: View?)
    }

    companion object {
        fun newInstance() = InitialQuestionsFragment()
    }
}