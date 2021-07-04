package com.rakudasoft.mytraining

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import androidx.activity.result.contract.ActivityResultContracts
import com.rakudasoft.mytraining.databinding.ActivityMainBinding
import com.rakudasoft.mytraining.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val textView = binding.textView
/*
        val startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) { result : ActivityResult ->
                    result.data?.let { data : Intent ->
                        val value = data.getStringExtra(getString(R.string.extra_username))
                        textView.text = value
                    }
                }
            }
        startForResult.launch(Intent(this, LoginActivity::class.java))
*/

        var intent = Intent(this, LoginActivity::class.java)
        startActivityForResult(intent, LoginActivity.REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            LoginActivity.REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        binding.textView.text = data?.getStringExtra(getString(R.string.extra_username))
                    }
                }
            }
        }
    }
}