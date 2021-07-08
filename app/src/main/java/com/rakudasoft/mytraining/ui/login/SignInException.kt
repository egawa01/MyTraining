package com.rakudasoft.mytraining.ui.login

import java.lang.Exception

open class SignInException(message : String, innerException: Exception?) : Exception(message, innerException) {
    constructor(innerException: Exception?) : this("Login failed." + innerException?.message, innerException) {
    }
}