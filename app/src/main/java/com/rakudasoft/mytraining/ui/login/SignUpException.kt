package com.rakudasoft.mytraining.ui.login

import java.lang.Exception

open class SignUpException(message : String, innerException: Exception?) : Exception(message, innerException) {
    constructor(innerException: Exception?) : this("User registration failed." + innerException?.message, innerException) {
    }
}