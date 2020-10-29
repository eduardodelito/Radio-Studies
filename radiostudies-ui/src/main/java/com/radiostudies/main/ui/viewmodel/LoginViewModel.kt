package com.radiostudies.main.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.radiostudies.main.common.viewmodel.BaseViewModel
import com.radiostudies.main.db.entity.UserEntity
import com.radiostudies.main.db.manager.DBManager
import com.radiostudies.main.ui.fragment.R
import com.radiostudies.main.ui.model.login.ErrorModel
import com.radiostudies.main.ui.model.login.LoginSuccessModel
import com.radiostudies.main.ui.model.login.LoginValidModel
import com.radiostudies.main.ui.model.login.LoginViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LoginViewModel @Inject constructor(var dbManager: DBManager) : BaseViewModel(),
    CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val login = MediatorLiveData<LoginViewState>()
    internal fun getLoginLiveData(): LiveData<LoginViewState> = login
    private var userName: String = EMPTY_STRING
    private var password: String = EMPTY_STRING

    fun insertUser() {
        // Load default user
        var users = mutableListOf<UserEntity>()
        users.add(
            UserEntity(
                0,
                "John",
                "Doe",
                "johndoe",
                "123456",
                "1234",
                "subCon",
                "userType",
                false,
                "cavite",
                Calendar.getInstance().time.toString()
            )
        )
        launch {
            insertUserToDB(users)
        }
    }

    private suspend fun insertUserToDB(users: List<UserEntity>) {
        withContext(Dispatchers.IO) {
            try {
                dbManager.insertUsers(users)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Textwatcher method for username.
     * @param text
     */
    fun onUsernameTextChanged(text: CharSequence) {
        userName = text.toString()
        validate()
    }

    /**
     * Textwatcher method for password.
     * @param text
     */
    fun onPasswordTextChanged(text: CharSequence) {
        password = text.toString()
        validate()
    }

    /**
     * Method to validate all text in the fields.
     * @param text
     */
    private fun validate() {
        if (userName.length < 6 || password.length < 6) {
            login.postValue(LoginValidModel(false))
        } else {
            login.postValue(LoginValidModel(true))
        }
    }

    /**
     * Login method using username and password.
     * @param userName
     * @param password
     */
    fun onLogin(userName: String, password: String) {
        launch {
            login(userName, password)
        }
    }

    /**
     * Suspend method to validate username and password into database,
     * if exist continue to login if not return error message.
     * @param userName
     * @param password
     */
    private suspend fun login(userName: String, password: String) {
        withContext(Dispatchers.IO) {
            try {
                if (dbManager.isUsernamePasswordValid(userName, password)) {
                    login.postValue(LoginSuccessModel(true))
                } else {
                    login.postValue(ErrorModel(R.string.invalid))
                }
            } catch (e: Exception) {
                login.postValue(ErrorModel(R.string.invalid))
                login.postValue(LoginSuccessModel(false))
                e.printStackTrace()
            }
        }
    }

    companion object {
        private const val EMPTY_STRING = ""
    }
}