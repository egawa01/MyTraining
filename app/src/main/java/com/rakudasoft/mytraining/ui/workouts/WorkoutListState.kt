package com.rakudasoft.mytraining.ui.workouts

import com.rakudasoft.mytraining.data.model.Workout

data class WorkoutListState (
    val list : List<Workout>? = null,
    val error : Exception? = null
        )