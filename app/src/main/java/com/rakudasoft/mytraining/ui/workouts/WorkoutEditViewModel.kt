package com.rakudasoft.mytraining.ui.workouts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rakudasoft.mytraining.R
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

        repository.deleteSuccessListener = {
            Log.d(logLevel, "WorkoutEditViewModel.init(): repository.deleteSuccessListener process")
            _workoutEditResultState.value = WorkoutEditResultState(deletedWorkoutId = it)
        }

        repository.deleteFailureListener = {
            Log.d(logLevel,"WorkoutEditViewModel.init(): repository.deleteFailureListener process")
            _workoutEditResultState.value = WorkoutEditResultState(error = it)
        }

        repository.getItemSuccessListener = {
            Log.d(logLevel, "WorkoutEditViewModel.init(): repository.getItemSuccessListener process")
            _workoutEditFormState.value = WorkoutEditFormState(
                name = it.name,
                nameError = getNameError(it.name),
                description = it.description,
                descriptionError = getDescriptionError(it.description),
                isFormValid = isFormValid(it.name, it.description)
            )
        }

        repository.getItemFailureListener = {
            Log.d(logLevel,"WorkoutEditViewModel.init(): repository.getItemFailureListener process")
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

    fun delete() {
        Log.d(logLevel, "WorkoutLEditViewModel.delete() process")
        if(workoutId != null) {
            repository.delete(workoutId)
        } else {
            throw IOException("workoutId Is null")
        }
    }

    fun formChanged(name : String, description : String) {
        Log.d(logLevel, "WorkoutEditViewModel.formChanged() process name=$name, description=$description")
        if(name != _workoutEditFormState.value?.name || description != _workoutEditFormState.value?.description) {
            Log.d(logLevel, "updating workoutEditFormState.")
            _workoutEditFormState.value = WorkoutEditFormState(
                name = name,
                nameError = getNameError(name),
                description = description,
                descriptionError = getDescriptionError(description),
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
        return getNameError(name) == null && getDescriptionError(description) == null
    }

    private fun getNameError(name : String) : Int? {
        return if(name.isNotEmpty()) {
            null
        } else {
            R.string.error_form_mandatory
        }
    }

    private fun getDescriptionError(description : String) : Int? {
        return null
    }

}