package com.rakudasoft.mytraining

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class WorkoutListViewAdapter(context : Context, private val list:List<String>)
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
        val name = list[position]
        Log.d("VERBOSE", "onBindViewHolder $position $name")
        holder.workoutName.text = name
    }
}