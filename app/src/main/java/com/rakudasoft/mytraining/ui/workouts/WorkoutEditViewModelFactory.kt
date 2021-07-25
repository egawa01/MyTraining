package com.rakudasoft.mytraining.ui.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rakudasoft.mytraining.data.WorkoutDataSource
import com.rakudasoft.mytraining.data.WorkoutRepository

class WorkoutEditViewModelFactory(private val userId : String, private val workoutId : String?) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutEditViewModel::class.java)) {
            return WorkoutEditViewModel(
                repository = WorkoutRepository(
                    dataSource = WorkoutDataSource(userId)
                ),
                workoutId = workoutId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}