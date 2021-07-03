package com.rakudasoft.mytraining.data

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.rakudasoft.mytraining.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */

typealias OnCompletedListener = (Result<LoggedInUser>) -> Unit

class LoginDataSource {

    var completedListener : OnCompletedListener? = null

    var failureListener : OnFailureListener? = null
    
    fun login(username: String, password: String) {

        try {
            // TODO: handle loggedInUser authentication
            val auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(username,password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user = LoggedInUser(username,username)
                        completedListener?.invoke(Result.Success(user))
                    } else {
                        completedListener?.invoke(Result.Error(IOException("Error Logging In", it.exception)))
                    }
                }
                .addOnFailureListener {
                    completedListener?.invoke(Result.Error(IOException("Error Logging In", it)))
                }

            //val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
            //return Result.Success(fakeUser)
        } catch (e: Throwable) {
            completedListener?.invoke(Result.Error(IOException("Error logging in", e)))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}