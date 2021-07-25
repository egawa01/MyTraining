package com.rakudasoft.mytraining.ui.workouts

import android.app.Activity
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
        const val EXTRA_WORKOUT_ID = "com.rakudasoft.mytraining.WORKOUT_ID"
        const val EXTRA_USER_ID = "com.rakudasoft.mytraining.USER_ID"
    }

    private lateinit var binding: ActivityWorkoutEditBinding
    private lateinit var viewModel : WorkoutEditViewModel
    private lateinit var userId : String
    private var workoutId : String? = null
    private val logLevel = "VERBOSE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_edit)

        Log.d(logLevel, "WorkoutEditActivity onCreate process")

        val userIdN : String? = intent.getStringExtra(WorkoutEditActivity.EXTRA_USER_ID)
        if (userIdN == null) {
            Log.d(logLevel, "userId is null")
            finish()
        } else {
            userId = userIdN
            Log.d(logLevel, "userId is $userId")
        }

        workoutId = intent.getStringExtra(EXTRA_WORKOUT_ID)
        Log.d(logLevel, "workoutId id $workoutId")

        binding = ActivityWorkoutEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val textName = binding.textName
        val textDescription = binding.textDescription
        val buttonOk = binding.buttonOK
        val buttonCancel = binding.buttonCancel

        viewModel = ViewModelProvider(this, WorkoutEditViewModelFactory(userId, workoutId))
            .get(WorkoutEditViewModel::class.java)

        viewModel.workoutEditResultState.observe(this@WorkoutEditActivity, Observer {
            val workoutEditResultState = it ?: return@Observer

            Log.d(logLevel, "WorkoutEditActivity viewModel.workoutEditResultState.observe process")

            if(workoutEditResultState.workout != null) {
                Log.d(logLevel, "workout is created/updated successfully.")
                setResult(Activity.RESULT_OK)
                finish()
            }

            if(workoutEditResultState.deletedWorkoutId != null) {
                Log.d(logLevel, "workout is deleted successfully.")
                setResult(Activity.RESULT_OK)
                finish()
            }

            if (workoutEditResultState.error != null) {
                Log.d(logLevel, "Error: " + workoutEditResultState.error.toString())
                val message = workoutEditResultState.error.message
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.title_alert_error))
                    .setMessage(message)
                    .setPositiveButton(getString(R.string.label_ok)){ _, _ ->
                        Log.d(logLevel, "OK is selected.")
                    }
                    .show()
            }
        })

        viewModel.workoutEditFormState.observe(this@WorkoutEditActivity, Observer {
            val workoutEditFormState = it ?: return@Observer

            Log.d(logLevel, "WorkoutEditActivity viewModel.workoutEditFormState.observe process")

            if(workoutEditFormState.name != null && workoutEditFormState.name != textName.text.toString()) {
                textName.setText(workoutEditFormState.name)
            }

            if(workoutEditFormState.description != null && workoutEditFormState.description != textDescription.text.toString()) {
                textDescription.setText(workoutEditFormState.description)
            }

            if(workoutEditFormState.nameError != null) {
                textName.error = getString(workoutEditFormState.nameError)
            }

            if(workoutEditFormState.descriptionError != null) {
                textDescription.error = getString(workoutEditFormState.descriptionError)
            }

            buttonOk.isEnabled = workoutEditFormState.isFormValid
        })

        buttonOk
            .setOnClickListener {
                Log.d(logLevel, "WorkoutEditActivity buttonOK onCreate process")
                viewModel.execute()
            }

        buttonCancel
            .setOnClickListener {
                Log.d(logLevel, "WorkoutEditActivity buttonCancel onCreate process")
                setResult(Activity.RESULT_CANCELED)
                finish()
            }

        textName
            .doAfterTextChanged {
                Log.d(logLevel, "textName.doAfterTextChanged process")
                viewModel.formChanged(textName.text.toString(), textDescription.text.toString())
            }

        textDescription
            .doAfterTextChanged {
                Log.d(logLevel, "textDescription.doAfterTextChanged process")
                viewModel.formChanged(textName.text.toString(), textDescription.text.toString())
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(workoutId != null) {
            menuInflater.inflate(R.menu.workout_edit, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.delete -> {
                Log.d(logLevel, "WorkoutEditActivity.onOptionsItemSelected process")
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.delete_text))
                    .setMessage(getString(R.string.message_delete_confirmation))
                    .setPositiveButton(getString(R.string.label_yes)) { _, _ ->
                        Log.d(logLevel, "Yes is selected.")
                        if(workoutId != null) {
                            Log.d(logLevel, "viewMode.delete calling")
                            viewModel.delete()
                        } else {
                            Log.d(logLevel, "createNew mode delete is cancel")
                            setResult(Activity.RESULT_CANCELED)
                            finish()
                        }
                    }
                    .setNegativeButton(getString(R.string.label_no)) { _, _ ->
                        Log.d(logLevel, "No is selected.")
                    }
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}