package com.rakudasoft.mytraining.ui.workouts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import com.rakudasoft.mytraining.R
import com.rakudasoft.mytraining.data.model.Workout
import com.rakudasoft.mytraining.databinding.ActivityWorkoutEditBinding
import kotlinx.coroutines.flow.callbackFlow
import java.util.*

class WorkoutEditActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE = 2000
    }

    private lateinit var binding: ActivityWorkoutEditBinding
    private lateinit var viewModel : WorkoutEditViewModel
    private val logLevel = "VERBOSE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_edit)

        Log.d(logLevel, "WorkoutEditActivity onCreate process")

        val userId : String? = intent.getStringExtra(getString(R.string.extra_username))

        if (userId == null) {
            Log.d(logLevel, "userId is null")
            finish()
        }

        binding = ActivityWorkoutEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val textName = binding.textName
        val textDescription = binding.textDescription
        val buttonOk = binding.buttonOK
        val buttonCancel = binding.buttonCancel

        if(userId != null) {
            viewModel = ViewModelProvider(this, WorkoutEditViewModelFactory(userId))
                .get(WorkoutEditViewModel::class.java)

            viewModel.workoutEditResultState.observe(this@WorkoutEditActivity, Observer {
                val workoutEditResultState = it ?: return@Observer

                Log.d(logLevel, "WorkoutEditActivity viewModel.workoutEditResultState.observe process")

                if(workoutEditResultState.workout != null) {
                    Log.d(logLevel, "workout is created successfully.")
                    finish()
                }

                if (workoutEditResultState.error != null) {
                    Log.d(logLevel, "Error")
                }
            })

            viewModel.workoutEditFormState.observe(this@WorkoutEditActivity, Observer {
                val workoutEditFormState = it ?: return@Observer

                Log.d(logLevel, "WorkoutEditActivity viewModel.workoutEditFormState.observe process")

                if(workoutEditFormState.isNameValid) {
                    textName.error = ""
                } else {
                    textName.error = "Error"
                }

                if(workoutEditFormState.isDescriptionValid) {
                    textDescription.error = ""
                } else {
                    textDescription.error = "Error"
                }

                buttonOk.isEnabled = workoutEditFormState.isFormValid
            })
        } else {
            Log.d(logLevel, "userId is null")
            finish()
        }

        buttonOk
            .setOnClickListener {
                Log.d(logLevel, "WorkoutEditActivity buttonOK onCreate process")

                val workout = Workout(
                    id = UUID.randomUUID().toString(),
                    name = textName.text.toString(),
                    description = textDescription.text.toString()
                )
                viewModel.create(workout)
            }

        buttonCancel
            .setOnClickListener {
                Log.d(logLevel, "WorkoutEditActivity buttonCancel onCreate process")

                finish()
            }

        textName
            .doAfterTextChanged {
                viewModel.formChanged(textName.text.toString(), textDescription.text.toString())
            }
    }
}