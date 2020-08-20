package com.radiostudies.main.ui.model

/**
 * Created by eduardo.delito on 8/20/20.
 */
sealed class LoginViewState
class LoginValidModel(var isValid: Boolean) : LoginViewState()
class LoginSuccessModel(var isSuccess: Boolean) : LoginViewState()
class ErrorModel(val message: Int) : LoginViewState()