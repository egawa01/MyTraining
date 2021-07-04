package com.rakudasoft.mytraining.data

import com.rakudasoft.mytraining.data.model.LoggedInUser

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {
    var signInCompletedListner : OnSignInCompletedListener? = null

    var signUpCompletedListener : OnSignUpCompletedListener? = null

    var resetPasswordCompletedListener : OnResetPasswordCompletedListener? = null

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null

        dataSource.signInCompletedListener = {
            if (it is Result.Success) {
                this.user = it.data as LoggedInUser
            }
            signInCompletedListner?.invoke(it)
        }

        dataSource.signUpCompletedListener = {
            signUpCompletedListener?.invoke(it)
        }

        dataSource.resetPasswordCompletedListner = {
            resetPasswordCompletedListener?.invoke(it)
        }

    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(username: String, password: String) {
        // handle login
        dataSource.login(username, password)
    }

    fun register(username: String, password: String) {
        // handle register
        dataSource.register(username, password)
    }

    fun resetPassword(username : String) {
        dataSource.resetPassword(username)
    }
}