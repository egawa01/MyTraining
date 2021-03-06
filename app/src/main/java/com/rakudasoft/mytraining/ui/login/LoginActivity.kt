package com.rakudasoft.mytraining.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import com.rakudasoft.mytraining.databinding.ActivityLoginBinding

import com.rakudasoft.mytraining.R
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE = 1000
    }

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("VERBOSE", "LoginActivity OnCreate")

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val login = binding.login
        val loading = binding.loading
        val register = binding.regiser
        val message = binding.message
        val resetPassword = binding.resetPassword

        message.text = ""

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            Log.d("VERBOSE", "loginFormState changed")

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid
            register.isEnabled = loginState.isDataValid
            resetPassword.isEnabled = loginState.usernameError == null

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            Log.d("VERBOSE", "loginResult changed")

            // hide loading
            loading.visibility = View.GONE

            // update message or finish
            when {
                loginResult.error != null -> {
                    Log.d("VERBOSE", "loginResult error")
                    message.text = getMessage(loginResult.error)
                }
                loginResult.resetPasswordSuccess != null -> {
                    Log.d("VERBOSE", "loginResult reset password")
                    message.text = getString(R.string.message_reset_password_success)
                }
                loginResult.registerSuccess != null -> {
                    Log.d("VERBOSE", "loginResult registered")
                    message.text = getString(R.string.message_register_success)
                }
                loginResult.loginSuccess != null -> {
                    Log.d("VERBOSE", "loginResult logged in")
                    val intent = Intent()
                    intent.putExtra(getString(R.string.extra_username), loginResult.loginSuccess.displayName)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        login.setOnClickListener {
            // clear message
            message.text = ""

            // loading view
            loading.visibility = View.VISIBLE

            // login
            loginViewModel.login(username.text.toString(), password.text.toString())
        }

        register.setOnClickListener {
            // clear message
            message.text = ""

            // loading view
            loading.visibility = View.VISIBLE

            // login
            loginViewModel.register(username.text.toString(), password.text.toString())
        }

        resetPassword.setOnClickListener {
            // clear message
            message.text = ""

            // loading view
            loading.visibility = View.VISIBLE

            // login
            loginViewModel.resetPassword(username.text.toString())
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }
        }
    }

    private fun getMessage(error: Exception): String? {
        when {
            error is NotVerifiedSignInException -> {
                return getString(R.string.error_sign_in_not_verified)
            }
            error is SignInException -> {
                return getString(R.string.error_sign_in_failure)
            }
            error is SignUpException -> {
                return getString(R.string.error_sign_up_failure)
            }
            error is ResetPasswordException -> {
                return getString(R.string.error_reset_password_failure)
            }
            else -> {
                return getString(R.string.error_unknown)
            }
        }

    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}