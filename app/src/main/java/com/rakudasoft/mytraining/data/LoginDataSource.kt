package com.rakudasoft.mytraining.data

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
            // TODO: handle loggedInUser authentication
            auth.signInWithEmailAndPassword(username,password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user : FirebaseUser? = (it.result as AuthResult).user
                        if (user!!.isEmailVerified) {
                            signInCompletedListener?.invoke(Result.Success(LoggedInUser(username, username)))
                        } else {
                            user.sendEmailVerification()
                            signInCompletedListener?.invoke(Result.Error(IOException("Verification Failure")))
                        }
                    } else {
                        signInCompletedListener?.invoke(Result.Error(IOException("Log In Failure", it.exception)))
                    }
                }
        } catch (e: Throwable) {
            signInCompletedListener?.invoke(Result.Error(IOException("Log In Error", e)))
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
                        signUpCompletedListener?.invoke(Result.Error(IOException("User Registration Failure", it.exception)))
                    }
                }
        }
        catch (e : Exception) {
            Log.d("Error", e.toString())
            signUpCompletedListener?.invoke(Result.Error(IOException("User Registration Error", e)))
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
                        resetPasswordCompletedListner?.invoke(Result.Error(IOException("Reset Password Failure.",it.exception)))
                    }
                }
        }
        catch (e : Exception) {
            Log.d("Error", e.toString())
            resetPasswordCompletedListner?.invoke(Result.Error(IOException("Reset Password Error", e)))
        }
    }
}