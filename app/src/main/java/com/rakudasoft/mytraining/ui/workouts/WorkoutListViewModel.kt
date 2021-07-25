package com.rakudasoft.mytraining.ui.workouts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rakudasoft.mytraining.data.WorkoutRepository
import com.rakudasoft.mytraining.data.model.Workout

class WorkoutListViewModel(private val repository: WorkoutRepository) : ViewModel() {

    private val _workoutListState = MutableLiveData<WorkoutListState>()
    val workoutListState : LiveData<WorkoutListState> = _workoutListState

    private val logLevel = "VERBOSE"
    init {
        repository.getSuccessListener = {
            Log.d(logLevel, "WorkoutListViewModel: repository.getSuccessListener process")
            _workoutListState.value = WorkoutListState(list = it)
        }

        repository.getFailureListener = {
            Log.d(logLevel, "WorkoutListViewModel: repository.getSFailureListener process")
            _workoutListState.value = WorkoutListState(error = it)
        }
    }

    fun getAll() {
        Log.d(logLevel, "WorkoutListViewModel.getAll() process")
        repository.getAll()
    }
}