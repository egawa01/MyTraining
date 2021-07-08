package com.rakudasoft.mytraining.ui.login

class ResetPasswordException(message : String, innerException: java.lang.Exception?) : Exception(message, innerException)  {
    constructor(innerException: Exception?) : this("Resetting password failed." + innerException?.message, innerException)
}