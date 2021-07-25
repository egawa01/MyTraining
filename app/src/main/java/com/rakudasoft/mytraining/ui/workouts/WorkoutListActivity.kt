package com.rakudasoft.mytraining.ui.workouts

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rakudasoft.mytraining.R
import com.rakudasoft.mytraining.databinding.ActivityWorkoutListBinding
import com.rakudasoft.mytraining.ui.login.LoginActivity
import com.rakudasoft.mytraining.ui.login.LoginViewModel
import com.rakudasoft.mytraining.ui.login.LoginViewModelFactory

class WorkoutListActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityWorkoutListBinding
    private lateinit var viewModel : WorkoutListViewModel
    lateinit var userId : String

    private val logLevel = "VERBOSE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(logLevel, "WorkoutListActivity OnCreate")

        binding = ActivityWorkoutListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val userIdN : String? = intent.getStringExtra(getString(R.string.extra_username))
        if(userIdN == null) {
            Log.d(logLevel, "userId is null")
            finish()
        }
        else {
            userId = userIdN
            Log.d(logLevel, "userId is $userId")
        }

        viewModel = ViewModelProvider(this, WorkoutListViewModelFactory(userId))
            .get(WorkoutListViewModel::class.java)


        viewModel.workoutListState.observe(this@WorkoutListActivity, Observer {
            val workoutListState = it ?: return@Observer

            Log.d(logLevel, "WorkoutListActivity viewModel observe process")

            if(it.error != null) {
                Log.d(logLevel, "WorkoutListActivity viewModel observe error result process")
            }

            if(it.list != null) {
                for (item in it.list) {
                    Log.d(logLevel, "list item $item")
                }
                val adapter = WorkoutListViewAdapter(this, it.list)
                val layoutManager = LinearLayoutManager(this)

                // アダプターとレイアウトマネージャーをセット
                val recyclerView: RecyclerView = findViewById(R.id.workoutListRecyclerView)
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = adapter
                //recyclerView.setHasFixedSize(true)
            }


        })

        binding.fab.setOnClickListener {
            Log.d(logLevel, "fab.onClick process")
            val intent = Intent(this, WorkoutEditActivity::class.java)
            intent.putExtra(WorkoutEditActivity.EXTRA_USER_ID, userId)
            startActivityForResult(intent, WorkoutEditActivity.REQUEST_CODE)
        }


        viewModel.getAll()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(logLevel, "WorkoutListActivity.onActivityResult process")

        when (requestCode) {
            WorkoutEditActivity.REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        Log.d(logLevel, "Result.OK")
                        viewModel.getAll()
                    }
                }
            }
        }
    }

}