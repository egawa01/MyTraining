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

    private val logLevel = "VERBOSE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(logLevel, "WorkoutListActivity OnCreate")

        binding = ActivityWorkoutListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val userId : String? = intent.getStringExtra(getString(R.string.extra_username))
        if(userId == null) {
            Log.d(logLevel, "userId is null")
            finish()
        }
        else {
            viewModel = ViewModelProvider(this, WorkoutListViewModelFactory(userId))
                .get(WorkoutListViewModel::class.java)
        }

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
            val intent = Intent(this, WorkoutEditActivity::class.java)
            intent.putExtra(getString(R.string.extra_username), userId)
            startActivityForResult(intent, WorkoutEditActivity.REQUEST_CODE)
        }

        viewModel.get()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            WorkoutEditActivity.REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        viewModel.get()
                    }
                }
            }
        }
    }

}