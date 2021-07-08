package com.rakudasoft.mytraining.data

import android.content.pm.SigningInfo
import android.content.res.Resources
import android.util.Log
import androidx.core.content.res.TypedArrayUtils.getString
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rakudasoft.mytraining.R
import com.rakudasoft.mytraining.data.model.LoggedInUser
import com.rakudasoft.mytraining.data.model.RegiseredUser
import com.rakudasoft.mytraining.ui.login.NotVerifiedSignInException
import com.rakudasoft.mytraining.ui.login.ResetPasswordException
import com.rakudasoft.mytraining.ui.login.SignInException
import com.rakudasoft.mytraining.ui.login.SignUpException
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */

class LoginDataSource {

    var signInCompletedListener : OnSignInCompletedListener? = null

    var signUpCompletedListener : OnSignUpCompletedListener? = null

    var resetPasswordCompletedListner : OnResetPasswordCompletedListener? = null

    private lateinit var auth : FirebaseAuth

    init {
        auth = FirebaseAuth.getInstance()
    }

    fun login(username: String, password: String) {

        try {
            Log.d("VERBOSE", "Login process started")

            auth.signInWithEmailAndPassword(username,password)
                .addOnCompleteListener {
                    Log.d("VERBOSE", "Login OnComplete")
                    if (it.isSuccessful) {
                        val user : FirebaseUser? = (it.result as AuthResult).user
                        if (user!!.isEmailVerified) {
                            Log.d("VERBOSE", "Login Success")
                            signInCompletedListener?.invoke(Result.Success(LoggedInUser(username, username)))
                        } else {
                            Log.d("VERBOSE", "Login Failure : Not verified")
                            user.sendEmailVerification()
                            signInCompletedListener?.invoke(Result.Error(NotVerifiedSignInException()))
                        }
                    } else {
                        Log.d("VERBOSE", "Login Failure process started")
                        val exception = it.exception
                        Log.d("VERBOSE", "Login Failure:$exception")
                        signInCompletedListener?.invoke(Result.Error(SignInException(exception)))
                    }
                }
        } catch (e: Throwable) {
            Log.d("VERBOSE", "Login Error:$e")
            signInCompletedListener?.invoke(Result.Error(SignInException(e as java.lang.Exception)))
        }
    }

    fun logout() {
        // TODO: revoke authentication
        auth.signOut()
    }

    fun register(username: String, password: String) {
        try {
            auth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        it.result?.user?.sendEmailVerification()

                        val user = RegiseredUser(username, username)
                        signUpCompletedListener?.invoke(Result.Success(user))
                    } else {
                        val exception = it.exception
                        Log.d("INFO", "User Registration Failure:$exception")
                        signUpCompletedListener?.invoke(Result.Error(SignUpException(exception)))
                    }
                }
        }
        catch (e : Exception) {
            Log.d("INFO", "User Registration Error:$e")
            signUpCompletedListener?.invoke(Result.Error(SignUpException(e as java.lang.Exception)))
        }
    }

    fun resetPassword(username : String) {
        try{
            auth.sendPasswordResetEmail(username)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user = RegiseredUser(username, username)
                        resetPasswordCompletedListner?.invoke(Result.Success(user))
                    } else {
                        val exception = it.exception
                        Log.d("INFO","Reset Password Failure:$exception")
                        resetPasswordCompletedListner?.invoke(Result.Error(ResetPasswordException(exception)))
                    }
                }
        }
        catch (e : Exception) {
            Log.d("INFO","Reset Password Failure:$e")
            resetPasswordCompletedListner?.invoke(Result.Error(ResetPasswordException(e as Exception)))
        }
    }
}