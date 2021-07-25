package com.rakudasoft.mytraining.ui.workouts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rakudasoft.mytraining.data.WorkoutRepository
import com.rakudasoft.mytraining.data.model.Workout
import java.io.IOException
import java.util.*

class WorkoutEditViewModel(private val repository: WorkoutRepository, private val workoutId : String?) : ViewModel() {

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
            Log.d(logLevel,"WorkoutEditViewModel.init(): repository.createFailureListener process")
            _workoutEditResultState.value = WorkoutEditResultState(error = it)
        }

        repository.updateSuccessListener = {
            Log.d(logLevel, "WorkoutEditViewModel.init(): repository.updateSuccessListener process")
            _workoutEditResultState.value = WorkoutEditResultState(workout = it)
        }

        repository.updateFailureListener = {
            Log.d(logLevel,"WorkoutEditViewModel.init(): repository.updateFailureListener process")
            _workoutEditResultState.value = WorkoutEditResultState(error = it)
        }

        repository.getItemSuccessListener = {
            Log.d(logLevel, "WorkoutEditViewModel.init(): repository.getItemSuccessListener process")
            _workoutEditFormState.value = WorkoutEditFormState(
                name = it.name,
                isNameValid = isNameValid(it.name),
                description = it.description,
                isDescriptionValid = isDescriptionValid(it.description),
                isFormValid = isFormValid(it.name, it.description)
            )
        }

        repository.updateFailureListener = {
            Log.d(logLevel,"WorkoutEditViewModel.init(): repository.updateFailureListener process")
            _workoutEditResultState.value = WorkoutEditResultState(error = it)
        }

        if(workoutId != null) {
            getItem(workoutId)
        }
    }

    fun execute() {
        Log.d(logLevel, "WorkoutLEditViewModel.execute() process")
        if(workoutId == null) {
            Log.d(logLevel, "WorkoutLEditViewModel.create() process")
            repository.create(getWorkout())
        } else {
            Log.d(logLevel, "WorkoutLEditViewModel.update() process")
            repository.update(getWorkout())
        }
    }

    fun formChanged(name : String, description : String) {
        Log.d(logLevel, "WorkoutEditViewModel.formChanged() process name=$name, description=$description")
        if(name != _workoutEditFormState.value?.name || description != _workoutEditFormState.value?.description) {
            Log.d(logLevel, "updating workoutEditFormState.")
            _workoutEditFormState.value = WorkoutEditFormState(
                name = name,
                isNameValid = isNameValid(name),
                description = description,
                isDescriptionValid = isDescriptionValid(description),
                isFormValid = isFormValid(name, description)
            )
        }
    }

    private fun getItem(workoutId : String) {
        Log.d(logLevel, "WorkoutLEditViewModel.getItem() process")
        repository.getitem(workoutId)
    }

    private fun getWorkout() : Workout {
        if(
            _workoutEditFormState.value?.name != null &&
            _workoutEditFormState.value?.description != null
        ) {
            val newWorkoutId : String = workoutId ?: UUID.randomUUID().toString()
            return Workout(
                id = newWorkoutId,
                name = _workoutEditFormState.value!!.name!!,
                description = _workoutEditFormState.value!!.description!!
            )
        } else {
            throw IOException()
        }
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