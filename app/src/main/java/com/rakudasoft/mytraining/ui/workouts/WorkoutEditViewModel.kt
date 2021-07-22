package com.rakudasoft.mytraining.ui.workouts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rakudasoft.mytraining.data.WorkoutRepository
import com.rakudasoft.mytraining.data.model.Workout

class WorkoutEditViewModel(private val repository: WorkoutRepository) : ViewModel() {

    private val _workoutEditResultState = MutableLiveData<WorkoutEditResultState>()
    val workoutEditResultState: LiveData<WorkoutEditResultState> = _workoutEditResultState

    private val _workoutEditFormState = MutableLiveData<WorkoutEditFormState>()
    val workoutEditFormState: LiveData<WorkoutEditFormState> = _workoutEditFormState

    private val logLevel = "VERBOSE"

    init {
        repository.createSuccessListener = {
            Log.d(logLevel, "WorkoutEditViewModel.init(): repository.createSuccessListener process")
            _workoutEditResultState.value = WorkoutEditResultState(workout = it)
        }

        repository.createFailureListener = {
            Log.d(
                logLevel,
                "WorkoutEditViewModel.init(): repository.createSFailureListener process"
            )
            _workoutEditResultState.value = WorkoutEditResultState(error = it)
        }
    }

    fun create(workout : Workout) {
        Log.d(logLevel, "WorkoutLEditViewModel.create() process")
        repository.create(workout)
    }

    fun formChanged(name : String, description : String) {
        _workoutEditFormState.value = WorkoutEditFormState(
            isNameValid = isNameValid(name),
            isDescriptionValid = isDescriptionValid(description),
            isFormValid = isFormValid(name, description)
        )
    }

    private fun isFormValid(name : String, description : String) : Boolean {
        return isNameValid(name) && isDescriptionValid(description)
    }

    private fun isNameValid(name : String) : Boolean {
        return name.isNotEmpty()
    }

    private fun isDescriptionValid(description : String) : Boolean {
        return true
    }

}