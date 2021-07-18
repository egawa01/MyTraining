package com.rakudasoft.mytraining.ui.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rakudasoft.mytraining.data.LoginDataSource
import com.rakudasoft.mytraining.data.LoginRepository
import com.rakudasoft.mytraining.data.WorkoutDataSource
import com.rakudasoft.mytraining.data.WorkoutRepository
import com.rakudasoft.mytraining.ui.login.LoginViewModel

class WorkoutListViewModelFactory(private val userId : String) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutListViewModel::class.java)) {
            return WorkoutListViewModel(
                repository = WorkoutRepository(
                    dataSource = WorkoutDataSource(userId)
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}