package com.rakudasoft.mytraining.ui.workouts

import com.rakudasoft.mytraining.data.model.Workout

data class WorkoutEditResultState (
    val workout : Workout? = null,
    val error : Exception? = null
    )