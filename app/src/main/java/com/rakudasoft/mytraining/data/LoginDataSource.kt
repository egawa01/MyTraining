package com.rakudasoft.mytraining.data

import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.rakudasoft.mytraining.data.model.LoggedInUser
import com.rakudasoft.mytraining.data.model.RegiseredUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */

typealias OnSignInCompletedListener = (Result<LoggedInUser>) -> Unit

typealias OnSignUpCompletedListener = (Result<RegiseredUser>) -> Unit

class LoginDataSource {

    var signInCompletedListener : OnSignInCompletedListener? = null

    var signUpCompletedListener : OnSignUpCompletedListener? = null

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
                        if ((it.result as AuthResult).user!!.isEmailVerified) {
                            val user = LoggedInUser(username, username)
                            signInCompletedListener?.invoke(Result.Success(user))
                        } else {
                            signInCompletedListener?.invoke(Result.Error(IOException("Login User has not verified.")))
                        }
                    } else {
                        signInCompletedListener?.invoke(Result.Error(IOException("Log in not Success", it.exception)))
                    }
                }
                .addOnFailureListener {
                    signInCompletedListener?.invoke(Result.Error(IOException("Log in Failure", it)))
                }

            //val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
            //return Result.Success(fakeUser)
        } catch (e: Throwable) {
            signInCompletedListener?.invoke(Result.Error(IOException("Error logging in", e)))
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
                        signUpCompletedListener?.invoke(Result.Error(IOException("User Registration Failure")))
                    }
                }
        }
        catch (e : Exception) {
            Log.d("Error", e.toString())
            signUpCompletedListener?.invoke(Result.Error(IOException("User Registration Error", e)))
        }
    }
}