package com.radiostudies.main.ui.fragment

import android.content.Context
import android.view.View
import com.radiostudies.main.common.fragment.BaseFragment
import com.radiostudies.main.common.util.hideKeyboard
import com.radiostudies.main.common.util.reObserve
import com.radiostudies.main.common.util.setViewVisibility
import com.radiostudies.main.ui.fragment.databinding.LoginFragmentBinding
import com.radiostudies.main.ui.model.ErrorModel
import com.radiostudies.main.ui.model.LoginSuccessModel
import com.radiostudies.main.ui.model.LoginValidModel
import com.radiostudies.main.ui.model.LoginViewState
import com.radiostudies.main.ui.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.login_fragment.*
import javax.inject.Inject

class LoginFragment : BaseFragment<LoginFragmentBinding, LoginViewModel>() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private var listener: LoginFragmentListener? = null

    @Inject
    override lateinit var viewModel: LoginViewModel

    override fun createLayout() = R.layout.login_fragment

    override fun getBindingVariable() =  BR.loginViewModel

    override fun initViews() {
        login_button.setOnClickListener {
            viewModel.onLogin(editText_username.text.toString(), editText_password.text.toString())
        }
    }

    override fun subscribeUi() {
        with(viewModel) {
            reObserve(getLoginLiveData(), ::onLoginStateChanged)
        }
    }

    /**
     * Method to update enable/disable login button.
     * @param state
     */
    private fun onLoginStateChanged(state: LoginViewState?) {
        when (state) {
            is LoginValidModel -> {
                login_button.isEnabled = state.isValid
                if (state.isValid) {
                    login_button.background =
                        resources.getDrawable(R.drawable.btn_selector_shape, null)
                } else {
                    login_button.setBackgroundColor(
                        resources.getColor(
                            R.color.disabledColorPrimary,
                            null
                        )
                    )
                }
            }

            is LoginSuccessModel -> {
                if (state.isSuccess) {
                    hideKeyboard()
                    view?.let { listener?.navigateToMainScreen(it) }
                    clearFields()
                }
            }

            is ErrorModel -> login_error_message.setViewVisibility(getString(state.message))
        }
    }

    /**
     * Reset fields.
     */
    private fun clearFields() {
        editText_username.setText("")
        editText_password.setText("")
        login_error_message.visibility = View.GONE
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LoginFragmentListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface LoginFragmentListener {
        fun navigateToMainScreen(view: View?)
    }
}
