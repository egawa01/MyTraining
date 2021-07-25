package com.rakudasoft.mytraining.ui.workouts

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rakudasoft.mytraining.R
import com.rakudasoft.mytraining.data.model.Workout

class WorkoutViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    private val workoutName : TextView = itemView.findViewById(R.id.workoutName)

    private val logLevel = "VERBOSE"

    private var _workout : Workout? = null

    fun setWorkout(workout : Workout) {
        val name = workout.name
        Log.d(logLevel, "SetWorkout $name")
        _workout = workout
        workoutName.text = name
    }

    fun getWorkout() : Workout? {
        Log.d(logLevel, "GetWorkout")
        return _workout
    }
}