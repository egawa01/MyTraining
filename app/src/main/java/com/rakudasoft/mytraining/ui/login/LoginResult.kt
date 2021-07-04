package com.rakudasoft.mytraining.ui.login

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val loginSuccess: LoggedInUserView? = null,
    val registerSuccess : String? = null,
    val error : String? = null
)