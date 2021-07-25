package com.rakudasoft.mytraining.ui.workouts

data class WorkoutEditFormState (
    val name : String? = null,
    val isNameValid : Boolean = false,
    val description : String? = null,
    val isDescriptionValid : Boolean = false,
    val isFormValid : Boolean = false
        )