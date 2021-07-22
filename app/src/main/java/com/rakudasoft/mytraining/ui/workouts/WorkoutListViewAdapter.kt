package com.rakudasoft.mytraining.ui.workouts

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rakudasoft.mytraining.R
import com.rakudasoft.mytraining.data.model.Workout

class WorkoutListViewAdapter(context : Context, private val list:List<Workout>)
    : RecyclerView.Adapter<WorkoutViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        Log.d("VERBOSE", "onCreateViewHolder")
        val view = layoutInflater.inflate(R.layout.workout_item, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun getItemCount(): Int {
        val size = list.size
        Log.d("VERBOSE", "getItemCount $size")
        return size
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val name = list[position].name
        Log.d("VERBOSE", "onBindViewHolder $position $name")
        holder.workoutName.text = name
    }
}