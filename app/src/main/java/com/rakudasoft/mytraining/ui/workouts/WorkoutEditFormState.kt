package com.rakudasoft.mytraining.ui.workouts

data class WorkoutEditFormState (
    val name : String? = null,
    val nameError : Int? = null,
    val description : String? = null,
    val descriptionError : Int? = null,
    val isFormValid : Boolean = false
        )