package com.rakudasoft.mytraining.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.rakudasoft.mytraining.data.LoginRepository
import com.rakudasoft.mytraining.data.Result

import com.rakudasoft.mytraining.R
import java.lang.Exception

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    init {
        loginRepository.signInCompletedListner = {
            if (it is Result.Success) {
                _loginResult.value =
                    LoginResult(loginSuccess = LoggedInUserView(displayName = it.data.displayName))
            } else if (it is Result.Error) {
                _loginResult.value =
                    LoginResult(error = it.exception)
            }
        }

        loginRepository.signUpCompletedListener = {
            if (it is Result.Success) {
                _loginResult.value =
                    LoginResult(registerSuccess = it.data.displayName)
            } else if (it is Result.Error) {
                _loginResult.value =
                    LoginResult(error = it.exception)
            }
        }

        loginRepository.resetPasswordCompletedListener = {
            if (it is Result.Success) {
                _loginResult.value =
                    LoginResult(resetPasswordSuccess = it.data.displayName)
            } else if (it is Result.Error) {
                _loginResult.value =
                    LoginResult(error = it.exception)
            }
        }

    }

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        loginRepository.login(username, password)
    }

    fun register(username: String, password: String) {
        // can be launched in a separate asynchronous job
        loginRepository.register(username, password)
    }

    fun resetPassword(username : String) {
        loginRepository.resetPassword(username)
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}