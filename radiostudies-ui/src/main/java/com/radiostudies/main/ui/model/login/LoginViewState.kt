package com.radiostudies.main.ui.model.login

/**
 * Created by eduardo.delito on 8/20/20.
 */
sealed class LoginViewState
class LoginValidModel(var isValid: Boolean) : LoginViewState()
class LoginSuccessModel(var isSuccess: Boolean) : LoginViewState()
class ErrorModel(val message: String?) : LoginViewState()